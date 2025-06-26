package com.liamfer.workoutTracker;

import com.liamfer.workoutTracker.domain.ExerciseEntity;
import com.liamfer.workoutTracker.domain.UserEntity;
import com.liamfer.workoutTracker.domain.WorkoutEntity;
import com.liamfer.workoutTracker.enums.WorkoutStatus;
import com.liamfer.workoutTracker.repository.UserRepository;
import com.liamfer.workoutTracker.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class Seeder {

    @Value("${seed.enabled:false}")
    private boolean seedEnabled;

    @Bean
    public CommandLineRunner seedDatabase(UserRepository userRepository,
                                          WorkoutRepository workoutRepository,
                                          PasswordEncoder passwordEncoder) {
        return args -> {
            if (!seedEnabled) return;
            if (userRepository.findByEmail("joao@example.com").isEmpty()) {
                UserEntity user = new UserEntity("João da Silva", "joao@example.com", passwordEncoder.encode("123456"));
                UserEntity createdUser = userRepository.save(user);

                WorkoutEntity workoutA = new WorkoutEntity();
                workoutA.setTitle("Treino A - Peito e Tríceps");
                workoutA.setDescription("Foco em exercícios compostos");
                workoutA.setScheduledAt(LocalDateTime.now().plusDays(1));
                workoutA.setStatus(WorkoutStatus.PENDING);
                workoutA.setOwner(createdUser);

                List<ExerciseEntity> exercisesA = List.of(
                        new ExerciseEntity(workoutA, "Supino Reto", 4, 10, 70, "Aumentar carga toda semana"),
                        new ExerciseEntity(workoutA, "Tríceps Pulley", 3, 12, 35, "")
                );
                workoutA.setExercises(exercisesA);


                WorkoutEntity workoutB = new WorkoutEntity();
                workoutB.setTitle("Treino B - Costas e Bíceps");
                workoutB.setDescription("Foco em largura das costas");
                workoutB.setScheduledAt(LocalDateTime.now().plusDays(3));
                workoutB.setStatus(WorkoutStatus.PENDING);
                workoutB.setOwner(createdUser);

                List<ExerciseEntity> exercisesB = List.of(
                        new ExerciseEntity(workoutB, "Puxada frente", 4, 12, 60, ""),
                        new ExerciseEntity(workoutB, "Rosca direta", 3, 10, 25, "Manter o controle na subida")
                );
                workoutB.setExercises(exercisesB);

                workoutRepository.saveAll(List.of(workoutA, workoutB));
            }
        };
    }
}
