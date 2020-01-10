package alanfx.cursomc.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import alanfx.cursomc.domain.Cidade;
import alanfx.cursomc.domain.Cliente;
import alanfx.cursomc.domain.Endereco;
import alanfx.cursomc.domain.enums.Perfil;
import alanfx.cursomc.domain.enums.TipoCliente;
import alanfx.cursomc.dto.ClienteDTO;
import alanfx.cursomc.dto.ClienteNewDTO;
import alanfx.cursomc.repositories.ClienteRepository;
import alanfx.cursomc.repositories.EnderecoRepository;
import alanfx.cursomc.security.UserSS;
import alanfx.cursomc.services.exceptions.AuthorizationException;
import alanfx.cursomc.services.exceptions.DataIntegrityException;
import alanfx.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	@Autowired
	private S3Service s3Service;
	
	public List<Cliente> findAll(){
		return repo.findAll();
	}
	
	public Cliente findByEmail(String email) {
		return repo.findByEmail(email);
	}
	
	//paginação
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente find(Integer id) {
		
		UserSS user = UserService.authenticated();
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: "+id+", Tipo: "+Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert(ClienteNewDTO objDto) {
		Cliente obj = fromDTO(objDto);
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);	
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getNumero(), objDto.getBairro(), objDto.getCep(), obj, cid);
		obj.setId(null);
		Cliente cliente = repo.save(obj);
		enderecoRepository.save(end);
		return cliente;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionadas");
		}
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		
		cli.getTelefones().add(objDto.getTelefone1());
		if(objDto.getTelefone2()!=null) {
			cli.getTelefones().add(objDto.getTelefone2());	
		}
		if(objDto.getTelefone3()!=null) {
			cli.getTelefones().add(objDto.getTelefone3());	
		}	
		return cli;
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		URI uri = s3Service.uploadFile(multipartFile);
		Optional<Cliente> obj = repo.findById(user.getId());
		Cliente cli = obj.get();
		cli.setImageUrl(uri.toString());
		repo.save(cli);
		
		return uri;
	}
}
