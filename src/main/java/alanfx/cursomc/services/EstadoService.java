package alanfx.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import alanfx.cursomc.domain.Estado;
import alanfx.cursomc.repositories.EstadoRepository;
import alanfx.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class EstadoService {

	@Autowired
	private EstadoRepository repo;
	
	public List<Estado> findAll(){
		return repo.findAllByOrderByNome();
	}
	
	public Estado find(Integer id) {
		Optional<Estado> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: "+id+", Tipo: "+Estado.class.getName()));
	}
	
	//paginação
//	public Page<Estado> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
//		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
//		return repo.findAll(pageRequest);
//	}
//	
//	
//	public Estado insert(Estado obj) {
//		obj.setId(null);
//		return repo.save(obj);
//	}
//	
//	public Estado update(Estado obj) {
//		Estado newObj = find(obj.getId());
//		updateData(newObj, obj);
//		return repo.save(newObj);
//	}
//	
//	private void updateData(Estado newObj, Estado obj) {
//		newObj.setNome(obj.getNome());
//	}
//	
//	public void delete(Integer id) {
//		find(id);
//		try {
//			repo.deleteById(id);
//		}catch(DataIntegrityViolationException e) {
//			throw new DataIntegrityException("Não é possível excluir um Estado que possui cidades");
//		}
//	}
//	
//	public Estado fromDTO(EstadoDTO objDto) {
//		return new Estado(objDto.getId(), objDto.getNome());
//	}
}
