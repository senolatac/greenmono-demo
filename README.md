# greenmono-demo

A demonstration project showcasing **Green Mono** development practices with AI-driven development using **Claude Code**.

## Table of Contents

- [About This Project](#about-this-project)
- [What is Claude Code?](#what-is-claude-code)
- [Green Mono Development](#green-mono-development)
- [Getting Started](#getting-started)
- [Development Skills & Best Practices](#development-skills--best-practices)
- [Working with Claude Code](#working-with-claude-code)
- [Contributing](#contributing)

## About This Project

This is a green-mono project, meaning it's developed from the ground up with AI assistance, emphasizing clean architecture, automated workflows, and modern development practices. The project serves as a reference implementation for teams looking to leverage AI-driven development methodologies.

## What is Claude Code?

**Claude Code** is an AI-powered coding assistant built on Anthropic's Claude Agent SDK. It's an interactive CLI tool that helps with software engineering tasks through natural language interaction.

### Key Features

- **Intelligent Code Analysis**: Understands your codebase structure and relationships
- **Automated Code Generation**: Writes production-quality code following best practices
- **Refactoring Support**: Helps improve code quality and maintainability
- **Testing Assistance**: Generates comprehensive unit and integration tests
- **Documentation**: Creates and maintains project documentation
- **Git Integration**: Manages commits, branches, and pull requests
- **Multi-file Operations**: Handles complex changes across multiple files

### Installation

```bash
# Install Claude Code CLI
npm install -g @anthropic-ai/claude-code

# Or use via npx
npx @anthropic-ai/claude-code
```

### Basic Usage

```bash
# Start Claude Code in your project directory
claude-code

# Get help
/help

# Example commands
"Add a new feature for user authentication"
"Refactor the payment module to use async/await"
"Write tests for the user service"
"Create a pull request for my changes"
```

## Green Mono Development

**Green Mono** (or "green-mono") refers to an AI-first development approach where projects are built from scratch with AI assistance, following these principles:

### Core Principles

1. **AI-Driven Development**: Leverage AI tools like Claude Code for code generation, reviews, and architectural decisions
2. **Clean Start**: Build with modern practices from day one, avoiding legacy technical debt
3. **Automated Quality**: Integrate automated testing, linting, and CI/CD from the beginning
4. **Documentation-First**: Maintain comprehensive documentation alongside code development
5. **Iterative Excellence**: Continuously improve through AI-assisted refactoring and optimization

### Development Workflow

```
Planning → AI-Assisted Implementation → Automated Testing → Review → Deploy
```

## Getting Started

### Prerequisites

- Node.js (v18 or higher recommended)
- Git
- Claude Code CLI
- Your preferred IDE (VS Code recommended)

### Quick Start

```bash
# Clone the repository
git clone https://github.com/senolatac/greenmono-demo.git
cd greenmono-demo

# Install dependencies (when available)
npm install

# Start development with Claude Code
claude-code
```

## Development Skills & Best Practices

### Working with Green Mono Projects

#### 1. Planning with AI

Before implementing features, use Claude Code to:
- Analyze requirements and propose architecture
- Identify potential challenges and solutions
- Create detailed implementation plans
- Break down complex tasks into manageable steps

**Example:**
```
"Help me design a user authentication system with JWT tokens"
"What's the best approach to implement real-time notifications?"
```

#### 2. Code Quality Standards

- **Clean Code**: Follow SOLID principles and design patterns
- **Type Safety**: Use TypeScript or similar type systems
- **Error Handling**: Implement comprehensive error handling
- **Security**: Follow OWASP guidelines and security best practices
- **Performance**: Write efficient, scalable code

#### 3. Testing Strategy

```
Unit Tests → Integration Tests → E2E Tests → Performance Tests
```

Use Claude Code to generate and maintain tests:
```
"Write unit tests for the UserService class"
"Create integration tests for the payment flow"
"Add E2E tests for the checkout process"
```

#### 4. Git Workflow

Follow a structured branching strategy:
- `main`: Production-ready code
- `develop`: Integration branch for features
- `feature/*`: Individual feature branches
- `ai/*`: AI-assisted development branches

**Commit Conventions:**
```
feat: Add new feature
fix: Bug fix
refactor: Code refactoring
test: Add or update tests
docs: Documentation updates
chore: Maintenance tasks
```

#### 5. Code Review Process

Use Claude Code for automated reviews:
```
"Review my changes for potential issues"
"Check this code for security vulnerabilities"
"Suggest improvements for performance"
```

#### 6. Documentation Practices

Maintain documentation at multiple levels:
- **README**: Project overview and setup instructions
- **Code Comments**: Explain complex logic and decisions
- **API Documentation**: Document endpoints and interfaces
- **Architecture Docs**: High-level system design
- **ADRs**: Architecture Decision Records

### Green Mono Best Practices Checklist

- [ ] Use Claude Code for planning before implementation
- [ ] Write tests alongside code development
- [ ] Document code and decisions as you go
- [ ] Run automated quality checks before commits
- [ ] Use AI-assisted code reviews
- [ ] Keep dependencies up to date
- [ ] Follow security best practices
- [ ] Implement CI/CD from the start
- [ ] Use semantic versioning
- [ ] Maintain a clear Git history

## Working with Claude Code

### Essential Commands

```bash
# Task Management
"Create a todo list for implementing user authentication"
"Mark the authentication task as complete"

# Code Generation
"Create a REST API endpoint for user registration"
"Add input validation to the login form"

# Refactoring
"Refactor this function to improve readability"
"Extract this logic into a separate service"

# Testing
"Write tests for the payment processor"
"Run all tests and fix any failures"

# Git Operations
"Create a commit with these changes"
"Create a pull request for feature/user-auth"

# Documentation
"Add JSDoc comments to this module"
"Update the README with the new features"
```

### Tips for Effective AI Collaboration

1. **Be Specific**: Provide clear, detailed requirements
2. **Iterate**: Start simple, then refine and enhance
3. **Review**: Always review AI-generated code
4. **Ask Questions**: Use Claude Code to understand code you're working with
5. **Leverage Context**: Claude Code understands your entire codebase
6. **Test Thoroughly**: Verify AI-generated code with comprehensive tests

### Common Workflows

#### Feature Development
```
1. "Analyze the requirements for feature X"
2. "Create an implementation plan"
3. "Implement the feature following the plan"
4. "Write tests for the new feature"
5. "Review the code for issues"
6. "Create a commit and pull request"
```

#### Bug Fixing
```
1. "Help me understand this bug in the authentication flow"
2. "Suggest fixes for the issue"
3. "Implement the fix"
4. "Add regression tests"
5. "Verify the fix doesn't break existing functionality"
```

#### Code Review
```
1. "Review this pull request for potential issues"
2. "Check for security vulnerabilities"
3. "Suggest performance improvements"
4. "Verify test coverage is adequate"
```

## Contributing

We welcome contributions to improve this demo project!

### Contribution Guidelines

1. **Fork the Repository**: Create your own fork
2. **Create a Branch**: Use the naming convention `feature/your-feature-name`
3. **Use Claude Code**: Leverage AI assistance for development
4. **Write Tests**: Ensure adequate test coverage
5. **Update Documentation**: Keep docs in sync with code
6. **Submit PR**: Create a pull request with a clear description

### Code of Conduct

- Be respectful and collaborative
- Follow the project's coding standards
- Write clean, maintainable code
- Document your changes
- Test thoroughly before submitting

## Resources

### Claude Code
- [Claude Code Documentation](https://docs.anthropic.com/claude-code)
- [GitHub Repository](https://github.com/anthropics/claude-code)

### Green Mono Development
- Best Practices Guide (coming soon)
- Architecture Patterns (coming soon)
- Case Studies (coming soon)

### Community
- GitHub Issues: Report bugs or request features
- Discussions: Share ideas and get help

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

Built with Claude Code | Green Mono Development | AI-Driven Excellence
