from flask import current_app, flash, redirect, render_template, request, session, url_for


def register_routes(app):
    @app.get("/")
    def root():
        if session.get("authenticated_user"):
            return redirect(url_for("dashboard"))
        return redirect(url_for("login"))

    @app.route("/login", methods=["GET", "POST"])
    def login():
        service = current_app.auth_service
        error = None

        if request.method == "POST":
            result = service.login(
                username=request.form.get("username", ""),
                password=request.form.get("password", ""),
            )
            if result.success:
                session["authenticated_user"] = result.username
                return redirect(url_for("dashboard"))
            error = result.message

        return render_template("login.html", error=error)

    @app.route("/forgot-password", methods=["GET", "POST"])
    def forgot_password():
        service = current_app.auth_service
        message = None

        if request.method == "POST":
            result = service.request_password_reset(
                identifier=request.form.get("identifier", ""),
                request_root=request.url_root.rstrip("/"),
            )
            message = result.message
            flash(message, "success")

        return render_template("forgot_password.html", message=message)

    @app.route("/reset-password", methods=["GET", "POST"])
    def reset_password():
        service = current_app.auth_service
        token = request.args.get("token", "")
        error = None

        if request.method == "POST":
            token = request.form.get("token", "")
            result = service.reset_password(token=token, new_password=request.form.get("new_password", ""))
            if result.success:
                flash("Password reset successfully. Please log in again.", "success")
                return redirect(url_for("login"))
            error = result.message

        token_result = service.validate_reset_token(token)
        if not token_result.success and request.method == "GET":
            error = token_result.message

        return render_template("reset_password.html", token=token, error=error)

    @app.get("/dashboard")
    def dashboard():
        username = session.get("authenticated_user")
        if not username:
            return redirect(url_for("login"))
        return render_template("dashboard.html", username=username)

    @app.get("/logout")
    def logout():
        session.pop("authenticated_user", None)
        flash("You have been logged out.", "success")
        return redirect(url_for("login"))