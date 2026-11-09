package br.com.floravel.floravel.domain.mae;

import br.com.floravel.floravel.common.exception.RegraDeNegocioException;
import br.com.floravel.floravel.domain.mae.dto.AtualizacaoMaeRequestDTO;
import br.com.floravel.floravel.domain.mae.dto.CadastroMaeRequestDTO;
import br.com.floravel.floravel.domain.mae.dto.MaeResponseDTO;
import br.com.floravel.floravel.domain.mae.exception.EmailJaCadastradoException;
import br.com.floravel.floravel.domain.mae.exception.MaeNaoEncontradaException;
import br.com.floravel.floravel.domain.usuario.TipoPerfil;
import br.com.floravel.floravel.domain.usuario.Usuario;
import br.com.floravel.floravel.domain.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaeService {

    private final MaeRepository maeRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MaeResponseDTO cadastrarMae(CadastroMaeRequestDTO dto) {
        String emailNormalizado = dto.getEmail().trim().toLowerCase();

        // Regra de Negócio: E-mail único na plataforma
        if (usuarioRepository.existsByEmail(emailNormalizado)) {
            throw new EmailJaCadastradoException(emailNormalizado);
        }

        // Regra de Negócio: Validação de faixa etária biológica compatível caso data informada
        validarDataNascimento(dto.getDataNascimento());

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome().trim())
                .email(emailNormalizado)
                .senha(passwordEncoder.encode(dto.getSenha()))
                .telefone(dto.getTelefone() != null ? dto.getTelefone().trim() : null)
                .fotoUrl(dto.getFotoUrl() != null ? dto.getFotoUrl().trim() : null)
                .cidade(dto.getCidade() != null ? dto.getCidade().trim() : null)
                .estado(dto.getEstado() != null ? dto.getEstado().trim() : null)
                .tipoPerfil(TipoPerfil.MAE)
                .build();

        Mae mae = Mae.builder()
                .usuario(usuario)
                .dataNascimento(dto.getDataNascimento())
                .build();

        Mae maeSalva = maeRepository.save(mae);
        return MaeResponseDTO.fromEntity(maeSalva);
    }

    @Transactional(readOnly = true)
    public List<MaeResponseDTO> listarTodas() {
        return maeRepository.findAll()
                .stream()
                .map(MaeResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public MaeResponseDTO buscarPorId(String id) {
        Mae mae = maeRepository.findById(id)
                .orElseThrow(() -> MaeNaoEncontradaException.porId(id));
        return MaeResponseDTO.fromEntity(mae);
    }

    @Transactional(readOnly = true)
    public MaeResponseDTO buscarPorEmail(String email) {
        String emailNormalizado = email.trim().toLowerCase();
        Mae mae = maeRepository.findByUsuarioEmail(emailNormalizado)
                .orElseThrow(() -> MaeNaoEncontradaException.porEmail(emailNormalizado));
        return MaeResponseDTO.fromEntity(mae);
    }

    @Transactional
    public MaeResponseDTO atualizarMae(String id, AtualizacaoMaeRequestDTO dto) {
        Mae mae = maeRepository.findById(id)
                .orElseThrow(() -> MaeNaoEncontradaException.porId(id));

        Usuario usuario = mae.getUsuario();

        if (dto.getNome() != null && !dto.getNome().isBlank()) {
            usuario.setNome(dto.getNome().trim());
        }
        if (dto.getTelefone() != null) {
            usuario.setTelefone(dto.getTelefone().trim());
        }
        if (dto.getFotoUrl() != null) {
            usuario.setFotoUrl(dto.getFotoUrl().trim());
        }
        if (dto.getCidade() != null) {
            usuario.setCidade(dto.getCidade().trim());
        }
        if (dto.getEstado() != null) {
            usuario.setEstado(dto.getEstado().trim());
        }
        if (dto.getDataNascimento() != null) {
            validarDataNascimento(dto.getDataNascimento());
            mae.setDataNascimento(dto.getDataNascimento());
        }

        Mae maeAtualizada = maeRepository.save(mae);
        return MaeResponseDTO.fromEntity(maeAtualizada);
    }

    @Transactional
    public void excluirMae(String id) {
        Mae mae = maeRepository.findById(id)
                .orElseThrow(() -> MaeNaoEncontradaException.porId(id));
        maeRepository.delete(mae);
    }

    private void validarDataNascimento(LocalDate dataNascimento) {
        if (dataNascimento == null) return;

        LocalDate hoje = LocalDate.now();
        if (dataNascimento.isAfter(hoje)) {
            throw new RegraDeNegocioException("A data de nascimento não pode ser futura.");
        }

        int idade = Period.between(dataNascimento, hoje).getYears();
        if (idade < 10) {
            throw new RegraDeNegocioException("Data de nascimento inválida: idade incompatível para cadastro materno.");
        }
        if (idade > 120) {
            throw new RegraDeNegocioException("Data de nascimento inválida: favor informar uma data válida.");
        }
    }
}
