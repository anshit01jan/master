Correctness	        Does each component behave as specified in requirements.md?
Security	        Are secrets excluded from output? Is user input validated?
Error Handling	    Are all API failures, missing files, and empty repos handled gracefully?
Accessibility	    Are keyboard navigation and screen-reader-friendly flows considered?
Test Coverage	    Do tests cover the happy path AND the 'Not Found' / missing-field edge cases?
Code Clarity	    Are function names self-explanatory? Is logic easy to follow without comments?
DRY Principle	    Is there duplicated logic that can be refactored into a shared function?
Dependency Safety	Does the review flag any known-vulnerable package versions?
Automation Evidence	Does the PR reference the actual latest full-suite result, not a stale or rewritten summary?
