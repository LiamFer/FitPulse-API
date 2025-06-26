package com.liamfer.workoutTracker.controller;

import com.liamfer.workoutTracker.DTO.APIResponseMessage;
import com.liamfer.workoutTracker.DTO.CreateWorkoutDTO;
import com.liamfer.workoutTracker.DTO.FinishedCountPerMonthDTO;
import com.liamfer.workoutTracker.DTO.UpdateWorkoutDTO;
import com.liamfer.workoutTracker.domain.WorkoutEntity;
import com.liamfer.workoutTracker.service.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workout")
@SecurityRequirement(name = "bearerAuth")
public class WorkoutController {
    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @Operation(summary = "Retorna os Treinos do Usuário com estrutura Paginada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados retornados com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<WorkoutEntity>> getWorkouts(@AuthenticationPrincipal UserDetails user,
                                                           Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(workoutService.getWorkouts(user, pageable));
    }

    @Operation(summary = "Retorna um Relatório dos Treinos concluídos do Usuário por Mês")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados retornados com sucesso")
    })
    @GetMapping("/report")
    public ResponseEntity<List<FinishedCountPerMonthDTO>> getWorkouts(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.status(HttpStatus.OK).body(workoutService.getWorkoutsReport(user));
    }

    @Operation(summary = "Cria um novo treino para o usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Treino criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutEntity.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Usuário Não Encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponseMessage.class)
                    ))
    })
    @PostMapping()
    public ResponseEntity<WorkoutEntity> addWorkout(@AuthenticationPrincipal UserDetails user,
                                                    @RequestBody @Valid CreateWorkoutDTO workout) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutService.addNewWorkout(user, workout));
    }

    @Operation(summary = "Atualiza as informações de um treino do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Treino atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutEntity.class)
                    )),
            @ApiResponse(responseCode = "401", description = "Você não pode acessar este recurso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponseMessage.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Pelo menos um campo deve ser informado para atualização",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponseMessage.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Recurso Não Encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponseMessage.class)
                    ))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<WorkoutEntity> updateWorkout(@AuthenticationPrincipal UserDetails user,
                                                       @PathVariable("id") Long id,
                                                       @RequestBody @Valid UpdateWorkoutDTO workout) {
        return ResponseEntity.status(HttpStatus.OK).body(workoutService.updateWorkout(user, id, workout));
    }
    
    @Operation(summary = "Deleta um treino do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Treino deletado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Você não pode acessar este recurso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponseMessage.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Recurso Não Encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponseMessage.class)
                    ))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@AuthenticationPrincipal UserDetails user,
                                              @PathVariable("id") Long id) {
        workoutService.deleteWorkout(user, id);
        return ResponseEntity.noContent().build();
    }
}
