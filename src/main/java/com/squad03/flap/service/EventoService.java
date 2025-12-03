package com.squad03.flap.service;

import com.squad03.flap.DTO.EventoDTO;
import com.squad03.flap.model.Evento;
import com.squad03.flap.model.Usuario;
import com.squad03.flap.repository.EventoRepository;
import com.squad03.flap.repository.UsuarioRepository;
import com.squad03.flap.util.SegurancaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Autowired
    private SegurancaUtils segurancaUtils;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar todos os eventos
    public List<EventoDTO> listarTodos() {
        return eventoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Buscar evento por ID
    public EventoDTO buscarPorId(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado com ID: " + id));
        return converterParaDTO(evento);
    }

    // Buscar eventos por data
    public List<EventoDTO> buscarPorData(LocalDate data) {
        return eventoRepository.findByData(data).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Buscar eventos por mês e ano
    public List<EventoDTO> buscarPorMesEAno(int mes, int ano) {
        return eventoRepository.findByMesAndAno(mes, ano).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Buscar eventos futuros
    public List<EventoDTO> buscarEventosFuturos() {
        return eventoRepository.findEventosFuturos(LocalDate.now()).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // ✅ CRIAR NOVO EVENTO COM SINCRONIZAÇÃO GOOGLE CALENDAR
    @Transactional
    public EventoDTO criar(EventoDTO eventoDTO) {
        validarEvento(eventoDTO);

        Evento evento = converterParaEntidade(eventoDTO);
        evento = eventoRepository.save(evento);

        // ✅ SINCRONIZAR COM GOOGLE CALENDAR
        // ✅ SINCRONIZAR COM GOOGLE CALENDAR
        try {
            String emailUsuario = segurancaUtils.getUsuarioLogadoEmail();
            Usuario usuario = usuarioRepository.findByEmail(emailUsuario).orElse(null);

            if (usuario != null && usuario.getGoogleCalendarConectado()) {
                LocalDateTime dataInicio;
                LocalDateTime dataFim;

                if (evento.getHorario() != null) {
                    // Evento com horário específico
                    dataInicio = LocalDateTime.of(evento.getData(), evento.getHorario());
                    dataFim = dataInicio.plusHours(1);
                } else {
                    // Evento de dia inteiro (sem horário)
                    dataInicio = LocalDateTime.of(evento.getData(), LocalTime.of(0, 0));
                    dataFim = LocalDateTime.of(evento.getData(), LocalTime.of(23, 59));
                }

                googleCalendarService.criarEvento(
                        usuario,
                        evento.getTitulo(),
                        evento.getDescricao(),
                        dataInicio,
                        dataFim
                );

                System.out.println("✅ Evento sincronizado com Google Calendar: " + evento.getTitulo());
            }
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao sincronizar com Google Calendar: " + e.getMessage());
            e.printStackTrace();
            // Não falha a criação do evento se der erro no Google
        }


        return converterParaDTO(evento);
    }

    // Atualizar evento
    @Transactional
    public EventoDTO atualizar(Long id, EventoDTO eventoDTO) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado com ID: " + id));

        validarEvento(eventoDTO);

        evento.setTitulo(eventoDTO.getTitulo());
        evento.setDescricao(eventoDTO.getDescricao());
        evento.setData(eventoDTO.getData());
        evento.setHorario(eventoDTO.getHorario());
        evento.setLocal(eventoDTO.getLocal());
        evento.setParticipantes(eventoDTO.getParticipantes());
        evento.setCor(eventoDTO.getCor());
        evento.setTipo(eventoDTO.getTipo());

        evento = eventoRepository.save(evento);

        return converterParaDTO(evento);
    }

    // Deletar evento
    @Transactional
    public void deletar(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new RuntimeException("Evento não encontrado com ID: " + id);
        }
        eventoRepository.deleteById(id);
    }

    // Validar dados do evento
    private void validarEvento(EventoDTO eventoDTO) {
        if (eventoDTO.getTitulo() == null || eventoDTO.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("O título do evento é obrigatório");
        }

        if (eventoDTO.getData() == null) {
            throw new IllegalArgumentException("A data do evento é obrigatória");
        }

        if (eventoDTO.getTipo() == null) {
            throw new IllegalArgumentException("O tipo do evento é obrigatório");
        }
    }

    // Converter Entidade para DTO
    private EventoDTO converterParaDTO(Evento evento) {
        EventoDTO dto = new EventoDTO();
        dto.setId(evento.getId());
        dto.setTitulo(evento.getTitulo());
        dto.setDescricao(evento.getDescricao());
        dto.setData(evento.getData());
        dto.setHorario(evento.getHorario());
        dto.setLocal(evento.getLocal());
        dto.setParticipantes(evento.getParticipantes());
        dto.setCor(evento.getCor());
        dto.setTipo(evento.getTipo());
        return dto;
    }

    // Converter DTO para Entidade
    private Evento converterParaEntidade(EventoDTO dto) {
        Evento evento = new Evento();
        evento.setTitulo(dto.getTitulo());
        evento.setDescricao(dto.getDescricao());
        evento.setData(dto.getData());
        evento.setHorario(dto.getHorario());
        evento.setLocal(dto.getLocal());
        evento.setParticipantes(dto.getParticipantes());
        evento.setCor(dto.getCor());
        evento.setTipo(dto.getTipo());
        return evento;
    }
}
