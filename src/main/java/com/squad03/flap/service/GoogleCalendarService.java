package com.squad03.flap.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.squad03.flap.model.Usuario;
import com.squad03.flap.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "FLAP Kanban";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private NetHttpTransport httpTransport;
    private GoogleAuthorizationCodeFlow flow;

    public GoogleCalendarService() {
        try {
            this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Erro ao inicializar Google HTTP Transport", e);
        }
    }

    private GoogleAuthorizationCodeFlow getFlow() {
        if (flow == null) {
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, JSON_FACTORY, clientId, clientSecret, SCOPES)
                    .setAccessType("offline")
                    .setApprovalPrompt("force")
                    .build();
        }
        return flow;
    }

    // ✅ Gerar URL de autorização OAuth
    public String getAuthorizationUrl(String userId) {
        GoogleAuthorizationCodeRequestUrl authUrl = getFlow()
                .newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .setState(userId); // Passa o ID do usuário no state

        return authUrl.build();
    }

    // ✅ Trocar código de autorização por tokens e salvar no banco
    public void handleCallback(String code, String userId) throws IOException {
        GoogleTokenResponse tokenResponse = getFlow()
                .newTokenRequest(code)
                .setRedirectUri(redirectUri)
                .execute();

        String refreshToken = tokenResponse.getRefreshToken();

        // Salvar refresh token no banco
        Usuario usuario = usuarioRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setGoogleRefreshToken(refreshToken);
        usuario.setGoogleCalendarConectado(true);
        usuarioRepository.save(usuario);

        System.out.println("✅ Google Calendar conectado para usuário: " + usuario.getEmail());
    }

    // ✅ Obter credencial do usuário usando refresh token
    // ✅ CORRIGIDO: Obter credencial do usuário usando refresh token
    private Credential getCredential(Usuario usuario) throws IOException {
        if (usuario.getGoogleRefreshToken() == null) {
            throw new RuntimeException("Usuário não conectou Google Calendar");
        }
        // Criar credential manualmente com o refresh token
        Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setTokenServerEncodedUrl("https://oauth2.googleapis.com/token")
                .setClientAuthentication(new ClientParametersAuthentication(clientId, clientSecret))
                .build()
                .setRefreshToken(usuario.getGoogleRefreshToken());

        // Forçar refresh do token
        credential.refreshToken();

        return credential;
    }

    // ✅ Atualizar evento existente no Google Calendar
    public Event atualizarEvento(Usuario usuario, String eventId, String titulo,
                                 String descricao, LocalDateTime dataInicio,
                                 LocalDateTime dataFim) throws IOException {

        Calendar service = getCalendarService(usuario);

        // Buscar evento existente
        Event event = service.events().get("primary", eventId).execute();

        // Atualizar campos
        event.setSummary(titulo);
        event.setDescription(descricao);

        DateTime start = new DateTime(
                Date.from(dataInicio.atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
        );
        event.setStart(new EventDateTime()
                .setDateTime(start)
                .setTimeZone("America/Sao_Paulo"));

        DateTime end = new DateTime(
                Date.from(dataFim.atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
        );
        event.setEnd(new EventDateTime()
                .setDateTime(end)
                .setTimeZone("America/Sao_Paulo"));

        Event updatedEvent = service.events()
                .update("primary", eventId, event)
                .execute();

        System.out.println("✅ Evento atualizado no Google Calendar: " + eventId);

        return updatedEvent;
    }



    // ✅ Criar cliente do Google Calendar
    public Calendar getCalendarService(Usuario usuario) throws IOException {
        Credential credential = getCredential(usuario);

        return new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    // ✅ Listar eventos do Google Calendar do usuário
    public List<Event> listarEventos(Usuario usuario, LocalDateTime inicio, LocalDateTime fim)
            throws IOException {

        Calendar service = getCalendarService(usuario);

        DateTime startDateTime = new DateTime(
                Date.from(inicio.atZone(ZoneId.systemDefault()).toInstant())
        );
        DateTime endDateTime = new DateTime(
                Date.from(fim.atZone(ZoneId.systemDefault()).toInstant())
        );

        Events events = service.events().list("primary")
                .setTimeMin(startDateTime)
                .setTimeMax(endDateTime)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        return events.getItems();
    }

    // ✅ Criar evento no Google Calendar
    public Event criarEvento(Usuario usuario, String titulo, String descricao,
                             LocalDateTime dataInicio, LocalDateTime dataFim) throws IOException {

        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Data de início e fim são obrigatórias");
        }

        Calendar service = getCalendarService(usuario);

        Event event = new Event()
                .setSummary(titulo)
                .setDescription(descricao);

        DateTime start = new DateTime(
                Date.from(dataInicio.atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
        );
        event.setStart(new EventDateTime()
                .setDateTime(start)
                .setTimeZone("America/Sao_Paulo"));

        DateTime end = new DateTime(
                Date.from(dataFim.atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
        );
        event.setEnd(new EventDateTime()
                .setDateTime(end)
                .setTimeZone("America/Sao_Paulo"));

        Event createdEvent = service.events()
                .insert("primary", event)
                .execute();

        System.out.println("✅ Evento criado no Google Calendar!");
        System.out.println("   ID: " + createdEvent.getId());
        System.out.println("   Link: " + createdEvent.getHtmlLink());

        return createdEvent;
    }


    // ✅ Desconectar Google Calendar
    public void desconectar(Usuario usuario) {
        usuario.setGoogleRefreshToken(null);
        usuario.setGoogleCalendarConectado(false);
        usuarioRepository.save(usuario);

        System.out.println("❌ Google Calendar desconectado para usuário: " + usuario.getEmail());
    }
}
