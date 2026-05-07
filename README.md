# Sweet Metric

Sweet Metric is a software metrics suite designed to help measure, estimate, and evaluate software projects. It implements key software engineering metrics, allowing developers and project managers to calculate the size, complexity, and stability of their software systems.

## Supported Metrics

This suite includes the implementation of the following software metrics:

### 1. Function Points (FP)
Function Point Analysis is a standard method used to measure the size of a software project based on the functionality it provides to the user. It evaluates the system from a functional perspective, independent of the programming language or technology used. 
- **What it measures:** The business functionality of an application (External Inputs, External Outputs, External Inquiries, Internal Logical Files, and External Interface Files).
- **Why it's useful:** Helps in estimating development effort, cost, and time required to build the software.

### 2. Use Case Points (UCP) / Unified Data Points
*Note: This is implemented as Use Case Points in the codebase.*
Use Case Points are used to estimate the software size and effort required for object-oriented software projects. It is based on the system's use cases and actors.
- **What it measures:** The complexity of the system's actors and use cases, combined with technical and environmental factors.
- **Why it's useful:** Provides a reliable estimation technique early in the software development lifecycle, especially for projects using UML (Unified Modeling Language).

### 3. Software Maturity Index (SMI)
The Software Maturity Index provides an indication of the stability of a software product based on changes made to it over time (from one release to another).
- **What it measures:** The ratio of unchanged modules to the total number of modules in the current release. It tracks additions, modifications, and deletions of software components.
- **Why it's useful:** It helps determine if a software project is stabilizing or if it is still undergoing significant volatility. A value approaching 1.0 indicates that the product is stabilizing.

## Getting Started

The application is built with Java and provides a graphical user interface (GUI) for inputting project data and calculating the respective metrics.

To run the application, compile the source files in the `src` directory and run the `Main` class.
