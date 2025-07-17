# Contributor Guide

## Development instructions

- Always include properly cljdoc formatted docstrings on the public
  functions in a namespace.
- Make functions private by default by using `(defn- ...)` instead of `(defn ...)`

## Dev Environment Tips

- The SPA front-end code is in the `src` directory.
- The Tauri backend code is in the `src-tauri` directory.

## Testing Instructions

- Run the build, tests and linters with `npm run ci`
