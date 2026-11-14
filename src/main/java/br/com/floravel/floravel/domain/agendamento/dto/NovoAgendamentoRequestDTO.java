package br.com.floravel.floravel.domain.agendamento.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NovoAgendamentoRequestDTO {

    @NotBlank(message = "O ID da mãe é obrigatório.")
    private String maeId;

    @NotBlank(message = "O ID da psicóloga é obrigatório.")
    private String psicologoId;

    @NotNull(message = "A data e horário de início da consulta são obrigatórios.")
    @Future(message = "O agendamento só pode ser realizado para uma data/horário futuro.")
    private LocalDateTime dataHoraInicio;

    private String observacoes;
}
