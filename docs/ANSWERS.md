# DevOps midterm — written answers (Saparbek Seidulla)

## Task 1.4 — Difference between > and >>
`>` redirects output to a file and **overwrites** it: the file is truncated first (created if missing).
`>>` redirects output and **appends** to the end of the file, keeping the existing content.
Example: `./scripts/Saparbek_Seidulla_system_report.sh > report.txt` keeps only the latest report,
while `... >> logs/Saparbek_Seidulla_system_report.log` accumulates every run in one log (the script uses `tee -a`, the append mode).

## Task 2.4 — Why .env files, passwords and DB credentials must not be committed
- Git keeps the full history: a secret committed once stays in every clone and in old commits even after it is deleted.
- Public (or later leaked) repositories are scanned by bots within minutes; leaked DB passwords or tokens give direct access to data and infrastructure.
- Credentials differ per environment (local, test, production); hardcoding them couples the code to one environment.
- Correct approach: commit only `.env.example` with placeholder values, list `.env` in `.gitignore`, pass secrets through environment variables, Docker secrets or Jenkins Credentials. In this project `.env` is ignored (`git check-ignore -v .env`) and the GitHub token is stored in Jenkins Credentials (id `github-credentials`).

## Task 4.3 — Explanation of the controlled pipeline failure
- On my own branch `test-saparbek-seidulla-pipeline` I changed the test `returnsNotFoundForMissingStudent` to expect HTTP 200 instead of 404.
- The API correctly returns 404 for a nonexistent student, so the assertion failed, Gradle exited with a non-zero code, the **Test** stage failed and Jenkins skipped **Docker Build** and **Verify** (the pipeline stops on failure, so no image is built from broken code).
- Fix: `git revert` of the faulty commit restored the correct expectation (404). The next run passed all stages and the image `saparbek-seidulla-student-api:v1.1-saparbek-seidulla` (version bumped in Part 3) was built and verified.
- Main and the shared environment were not touched: the error lived only on the test branch.
