#### Generating Assembly code:
`sbt run`

#### Compiling and running the executable:
`./scripts/compile_and_run.sh`

---
To change the entrypoint of the asm program from `_main` to e.g. `_start`, add `-e _start` to `ld` command in `scripts/compile_and_run.sh`