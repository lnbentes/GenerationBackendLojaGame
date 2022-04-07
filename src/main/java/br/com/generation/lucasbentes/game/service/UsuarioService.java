package br.com.generation.lucasbentes.game.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.generation.lucasbentes.game.model.Usuario;
import br.com.generation.lucasbentes.game.model.UsuarioLogin;
import br.com.generation.lucasbentes.game.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	public Optional<Usuario> cadastraUsuario(Usuario usuario){
		
		if(usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
			return Optional.empty();
		}
		
		usuario.setSenha(criptografarSenha(usuario.getSenha()));
		
		return Optional.of(usuarioRepository.save(usuario));
	}
	
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin){
		
		Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());
		
		if(usuario.isPresent()) {
			if(compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {
				
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get().setToken(geradorBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));
				usuarioLogin.get().setSenha(usuario.get().getSenha());
				
				return usuarioLogin;
			}
		}
		
		return Optional.empty();
	}
	
	public Optional<Usuario> atualizarUsuario (Usuario usuario) {   
			
			if (usuarioRepository.findById(usuario.getId()).isPresent()) {     
				
				Optional<Usuario> cadeUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());
				
				if ((cadeUsuario.isPresent()) && (cadeUsuario.get().getId() != usuario.getId())) {
					throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Já existente",null);
				}
				 
				usuario.setSenha(criptografarSenha(usuario.getSenha()));      
				
				return Optional.of(usuarioRepository.save(usuario));     
				}
			
			return Optional.empty(); 
		}
	
	//#########################################################################
	
	private String criptografarSenha(String senha){
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(senha);
	}

	
	private boolean compararSenhas(String senhaEntrada, String senhaBanco){
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.matches(senhaEntrada, senhaBanco);
	}
	
	
	private String geradorBasicToken(String usuario, String senha){
		
		String token = usuario + ":" + senha;
		
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		
		return "Basic " + new String(tokenBase64); 
	}
	
	
}
