package br.com.floravel.floravel.service;

import br.com.floravel.floravel.dto.request.AtualizacaoMaeRequestDTO;
import br.com.floravel.floravel.dto.request.CadastroMaeRequestDTO;
import br.com.floravel.floravel.dto.response.MaeResponseDTO;
import br.com.floravel.floravel.exception.EmailJaCadastradoException;
import br.com.floravel.floravel.exception.RecursoNaoEncontradoException;
import br.com.floravel.floravel.model.entity.Mae;
import br.com.floravel.floravel.model.entity.Usuario;
import br.com.floravel.floravel.model.enums.TipoPerfil;
import br.com.floravel.floravel.repository.MaeRepository;
import br.com.floravel.floravel.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaeService {

    private final MaeRepository maeRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MaeResponseDTO cadastrarMae(CadastroMaeRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException(dto.getEmail());
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome().trim())
                .email(dto.getEmail().trim().toLowerCase())
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
                .orElseThrow(() -> new RecursoNaoEncontradoException("Mãe não encontrada com o ID: " + id));
        return MaeResponseDTO.fromEntity(mae);
    }

    @Transactional(readOnly = true)
    public MaeResponseDTO buscarPorEmail(String email) {
        Mae mae = maeRepository.findByUsuarioEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Mãe não encontrada com o e-mail: " + email));
        return MaeResponseDTO.fromEntity(mae);
    }

    @Transactional
    public MaeResponseDTO atualizarMae(String id, AtualizacaoMaeRequestDTO dto) {
        Mae mae = maeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Mãe não encontrada com o ID: " + id));

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
            mae.setDataNascimento(dto.getDataNascimento());
        }

        Mae maeAtualizada = maeRepository.save(mae);
        return MaeResponseDTO.fromEntity(maeAtualizada);
    }

    @Transactional
    public void excluirMae(String id) {
        Mae mae = maeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Mãe não encontrada com o ID: " + id));
        maeRepository.delete(mae);
    }
}
