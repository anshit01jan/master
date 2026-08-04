package framework.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

/** Starts, health-checks, resets, and stops the local Flask app for the suite. Kept comment-only refreshed for PR scope visibility. */
public final class AppProcessManager {
    private static final HttpClient CLIENT = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    private static Process process;
    private static String expectedRunId;

    private AppProcessManager() {
    }

    public static synchronized void startApplication() {
        if (process != null && process.isAlive() && isHealthy()) {
            return;
        }

        stopApplication();

        Path workspace = Paths.get("").toAbsolutePath().normalize();
        expectedRunId = UUID.randomUUID().toString();
        String[] command = Arrays.stream(config.appCommand().trim().split("\\s+"))
            .filter(part -> !part.isBlank())
            .toArray(String[]::new);
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workspace.toFile());
        builder.redirectErrorStream(true);
        builder.inheritIO();
        builder.environment().put("NOTIFICATION_LOG", workspace.resolve(config.notificationLogPath()).toString());
        builder.environment().put("AUTOMATION_RUN_ID", expectedRunId);
        builder.environment().put("PORT", String.valueOf(resolvePort()));

        try {
            process = builder.start();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to start local application process", exception);
        }

        waitForHealth();
    }

    public static synchronized void resetApplicationState() {
        HttpRequest request = HttpRequest.newBuilder(URI.create(config.baseUrl() + "/__automation__/reset"))
            .timeout(Duration.ofSeconds(5))
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalStateException("Unable to reset application state. HTTP status: " + response.statusCode());
            }
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to reset application state", exception);
        }
    }

    public static synchronized void stopApplication() {
        if (process == null) {
            expectedRunId = null;
            return;
        }

        process.destroy();
        try {
            if (!process.waitFor(5, java.util.concurrent.TimeUnit.SECONDS)) {
                process.destroyForcibly();
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            process.destroyForcibly();
        } finally {
            process = null;
            expectedRunId = null;
        }
    }

    private static void waitForHealth() {
        Instant deadline = Instant.now().plusSeconds(30);
        while (Instant.now().isBefore(deadline)) {
            if (isHealthy()) {
                Logger.info("Application became healthy at " + config.baseUrl());
                return;
            }
            if (process != null && !process.isAlive()) {
                throw new IllegalStateException("Application process exited before the expected health check completed");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while waiting for the application to start", exception);
            }
        }

        stopApplication();
        throw new IllegalStateException("Application did not become healthy within 30 seconds");
    }

    private static boolean isHealthy() {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(config.baseUrl() + "/__automation__/health"))
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200 && response.body().contains("\"status\":\"ok\"");
        } catch (Exception exception) {
            return false;
        }
    }

    private static int resolvePort() {
        String baseUrl = config.baseUrl();
        try {
            return URI.create(baseUrl).getPort();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to resolve port from baseUrl: " + baseUrl, exception);
        }
    }
}