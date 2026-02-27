package es.brasatech.debit_wallet.adapter.in.web.controller;

import es.brasatech.debit_wallet.application.port_in.WorkspaceUseCase;
import es.brasatech.debit_wallet.domain.model.User;
import es.brasatech.debit_wallet.domain.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/fragments/w/{workspaceSlug}/team")
public class TeamViewController {

        private final WorkspaceUseCase workspaceUseCase;

        @GetMapping
        public String getTeam(@PathVariable String workspaceSlug, Model model) {
                var workspace = workspaceUseCase.getWorkspaceBySlug(workspaceSlug)
                                .orElseThrow(() -> new RuntimeException("Workspace not found: " + workspaceSlug));

                List<User> users = workspaceUseCase.getUsersInWorkspace(workspace.id());

                Map<UserRole, List<User>> usersByRole = users.stream()
                                .flatMap(user -> user.roles().stream()
                                                .map(role -> Map.entry(role, user)))
                                .collect(Collectors.groupingBy(
                                                Map.Entry::getKey,
                                                Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

                model.addAttribute("usersByRole", usersByRole);
                model.addAttribute("workspaceSlug", workspaceSlug);

                return "fragments/team/team :: team";
        }

        @org.springframework.web.bind.annotation.PostMapping("/add")
        public String addUser(@PathVariable String workspaceSlug,
                        @org.springframework.web.bind.annotation.RequestParam String name,
                        @org.springframework.web.bind.annotation.RequestParam String email,
                        @org.springframework.web.bind.annotation.RequestParam String username,
                        @org.springframework.web.bind.annotation.RequestParam String password,
                        @org.springframework.web.bind.annotation.RequestParam UserRole role,
                        Model model) {
                var workspace = workspaceUseCase.getWorkspaceBySlug(workspaceSlug)
                                .orElseThrow(() -> new RuntimeException("Workspace not found: " + workspaceSlug));

                workspaceUseCase.addUserToWorkspace(workspace.id(), name, email, username, password, role);

                return getTeam(workspaceSlug, model);
        }
}
