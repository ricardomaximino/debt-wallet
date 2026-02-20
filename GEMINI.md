# Debt Wallet - Developer & Architecture Guide (GEMINI.md)

Welcome, Agent! This document outlines the core principles, patterns, and long-term vision of the **Debit Wallet** application. Follow these guidelines to ensure consistency and maintainability.

## 🏗️ Architecture: Hexagonal (Ports & Adapters)

We follow a strict Hexagonal Architecture to decouple the business logic from infrastructure.

- **Domain Layer (`domain.model`)**: Pure Java logic and records. Contains entities, value objects, and business enums. **No frameworks allowed here.**
- **Application Layer (`application.service` / `application.port`)**: Defines the "What".
  - `ports.in`: Interfaces for use cases.
  - `ports.out`: Interfaces for infrastructure dependencies (persistence, external APIs).
  - `service`: Implementation of use cases.
- **Adapter Layer (`adapter`)**: Implementation of technology-specific details.
  - `adapter.in.web`: REST controllers, Thymeleaf controllers, and View resources (DTOs).
  - `adapter.out.jpa`: JPA entities and repositories.

> [!IMPORTANT]
> If shifting from JPA to MongoDB (or any other tech), only the `adapter.out` module should change. The domain and application logic must remain untouched.

## 🎨 UI/UX Strategy

Our goal is a fast, consistent, and maintainable SPA-like experience using standard Server-Side Rendering (SSR).

### 1. HTML Fragments (Thymeleaf)

- The application uses a single main layout (`wallet.html`).
- All sub-views (Dashboard, Wallet details, Debt details) are retrieved as **Thymeleaf Fragments** via AJAX calls.
- **Rules**:
  - Never return hardcoded HTML strings from Java.
  - Always return fragments using the `FragmentViewController`.

### 2. JavaScript

- Keep navigation and state logic in `wallet.js`.
- Each major view can have its own JS if complexity grows, but keep it lightweight.
- Use `fetch` to load fragments and replace the content of the `#app` container.

### 3. CSS & Aesthetic

- **Centralized CSS**: All custom styles go into `wallet.css`.
- **Framework**: Use **Bootstrap 5** for layout and standard components.
- **Theming**: Use CSS Variables (`--primary`, `--gray-200`, etc.) to facilitate future **Dark Mode** implementation.
- **Consistency**: High-contrast headers, rounded corners (12px), and clean shadows (box-shadow) are the current standard.

## 🌍 Internationalization (i18n)

- **Scope**: i18n is used for **static UI content only**.
- **Resources**: `src/main/resources/i18n/messages*.properties`.
- **Languages Supported**:
  - English (en) - Default
  - Spanish (es) - Spain variant
  - Portuguese (pt) - Brazil variant
- **Pattern**: Use `#{key}` in templates and the globe dropdown for locale switching.

## 🚀 SaaS Roadmap & Permissions

The application is designed to be a SaaS for lawyers, scaling from solo practitioners to large firms.

### 1. Plans & Roles

- **Core Plan (FREE)**: Manage personal/basic wallets. Feature-limited (e.g., wallet counts).
- **Professional/SaaS**: Multi-tenant, multi-workspace.
- **Roles**:
  - `ADMIN`: BackOffice management.
  - `LAWYER`: Manages their own workspace and can be invited to others.
  - `CLIENT`: Restricted access to view and optionally edit specific debt data.

### 2. Multi-Workspace

- A user (Lawyer) can belong to multiple workspaces.
- Access control must be fine-grained: a lawyer from "Firm A" might be invited to collaborate on a specific project in "Firm B" with limited permissions.
- **Security Check**: Always verify `user_id` or `workspace_id` ownership when accessing resources in adapters or services.

## 🛠️ Tech Stack

- **Backend**: Spring Boot 4.0.3+, Java 25.
- **Build**: Maven.
- **Persistence**: H2 (Dev), JPA/Hibernate.
- **Mapping**: MapStruct for domain-to-entity and domain-to-view conversions.
- **Safety**: Use records with compact constructors for null-safe collections.
