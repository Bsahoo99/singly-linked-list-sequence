# Singly-Linked List Sequence

A singly-linked-list-based sequence supporting standard sequence operations and deep cloning.

## Overview

This project implements `LinkedSequence`, a generic sequence backed by a singly-linked list. It supports indexed access, add, remove, and implements `Cloneable` so that clones are independent of the original. Internal structure is verified by dedicated tests.

## Requirements

- Java 8+

## Project Structure

```
.
├── src/
│   ├── edu/uwm/cs351/       # LinkedSequence
│   └── Test*.java            # JUnit 3 test suite
├── lib/
│   └── homework6.jar         # Locked-test framework
├── .gitignore
└── README.md
```

## Usage

### Compile
```bash
javac -cp lib/homework6.jar:. -d bin $(find src -name '*.java')
```

### Run Tests
```bash
java -cp bin:lib/homework6.jar:. org.junit.runner.JUnitCore TestLinkedSequence
java -cp bin:lib/homework6.jar:. org.junit.runner.JUnitCore TestInternals
java -cp bin:lib/homework6.jar:. org.junit.runner.JUnitCore TestEfficiency
```

## Key Classes

| Class | Responsibility |
|-------|----------------|
| `LinkedSequence` | Generic singly-linked sequence with indexed ops and deep clone |

## Author

Biswajeet Sahoo

## License

MIT License
