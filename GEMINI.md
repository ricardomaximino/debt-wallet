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

### 2. Multi-Workspace Isolation

The application enforces strict isolation between workspaces. A user can belong to multiple workspaces, but data (Wallets, Debts, etc.) is always scoped to a specific `workspace_id`.

- **Routing Pattern**: Workspace-specific features use the slug in the URL: `/w/{slug}/[feature]`.
- **Backend Context**:
  - **Controllers**: Use `workspaceSlug` as a `@PathVariable` to resolve the current workspace.
  - **API Requests**: Expect the `X-Workspace-Id` header (UUID) for any data-modifying or sensitive operations.
  - **Services**: All business logic methods MUST accept a `workspaceId` parameter. Methods relying on "default" workspaces are forbidden.
- **Frontend Context**:
  - `wallet.html` provides context via meta tags: `<meta name="workspace-slug">` and `<meta name="workspace-id">`.
  - `wallet.js` uses these tags to initialize the application state.

## 🛠️ Tech Stack & Patterns

- **Backend**: Spring Boot 4.0.3+, Java 25.
- **Mapping (MapStruct)**:
  - Always use `componentModel = "spring"` in `@Mapper` annotations.
  - Ensure `WritingPolicy.IGNORE` or explicit mapping for all workspace/user relationships.
- **CSRF Security**:
  - Every AJAX `POST/PUT/DELETE` must include the `X-CSRF-TOKEN` header.
  - Use the meta tags `<meta name="_csrf">` and `<meta name="_csrf_header">` defined in `landing.html` or `wallet.html`.
- **Internationalization (i18n)**:
  - Resource: `src/main/resources/i18n/messages*.properties`.
  - Pattern: Use `#{key}` in Thymeleaf and `[[#{key}]]` for script inlining.

## 🏗️ Development Checklist for New Features

1. **Domain**: Add `workspaceId` to the domain record.
2. **Persistence**: Ensure the JPA entity has a `WorkspaceEntity` relationship and the Repository filters by `workspaceId`.
3. **Service**: Pass `workspaceId` from the Port to the Service and down to the Persistence Adapter.
4. **Adapter In (Web)**:
   - Fragment routes: `/fragments/w/{workspaceSlug}/[feature]`.
   - API routes: Verify `X-Workspace-Id` header.
5. **Frontend**: Update `wallet.js` to handle the new fragment loading within the workspace context.
