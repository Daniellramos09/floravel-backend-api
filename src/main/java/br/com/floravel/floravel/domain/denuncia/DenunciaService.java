package br.com.floravel.floravel.domain.denuncia;

import br.com.floravel.floravel.common.exception.RegraDeNegocioException;
import br.com.floravel.floravel.domain.denuncia.dto.CriarDenunciaRequestDTO;
import br.com.floravel.floravel.domain.denuncia.dto.DenunciaResponseDTO;
import br.com.floravel.floravel.domain.usuario.Usuario;
import br.com.floravel.floravel.domain.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DenunciaService {

    private final DenunciaRepository denunciaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public DenunciaResponseDTO registrarDenuncia(CriarDenunciaRequestDTO dto) {
        Usuario denunciante = usuarioRepository.findById(dto.getDenuncianteUsuarioId())
                .orElseThrow(() -> new RegraDeNegocioException("Usuário denunciante não encontrado."));

        Denuncia denuncia = Denuncia.builder()
                .denunciante(denunciante)
                .denunciadoUsuarioId(dto.getDenunciadoUsuarioId())
                .salaId(dto.getSalaId())
                .motivo(dto.getMotivo().trim())
                .oQueFez(dto.getOQueFez().trim())
                .oQueSentiu(dto.getOQueSentiu().trim())
                .bloquearUsuario(dto.getBloquearUsuario() != null ? dto.getBloquearUsuario() : true)
                .status("PENDENTE")
                .build();

        Denuncia salva = denunciaRepository.save(denuncia);
        return DenunciaResponseDTO.fromEntity(salva);
    }

    @Transactional(readOnly = true)
    public List<DenunciaResponseDTO> listarPorDenunciante(String usuarioId) {
        return denunciaRepository.findByDenuncianteId(usuarioId).stream()
                .map(DenunciaResponseDTO::fromEntity)
                .toList();
    }
}
