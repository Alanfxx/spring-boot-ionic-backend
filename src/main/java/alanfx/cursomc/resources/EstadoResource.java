package alanfx.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import alanfx.cursomc.domain.Cidade;
import alanfx.cursomc.domain.Estado;
import alanfx.cursomc.dto.CidadeDTO;
import alanfx.cursomc.dto.EstadoDTO;
import alanfx.cursomc.services.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {

	@Autowired
	private EstadoService service;
//	@Autowired
//	private CidadeService cidadeService;
	
	@GetMapping
	public ResponseEntity<List<EstadoDTO>> findAll(){
		List<Estado> list = service.findAll();
		List<EstadoDTO> listDto = list.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Estado> find(@PathVariable Integer id) {
		
		Estado obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@GetMapping(value = "/{id}/cidades")
	public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Integer id) {
		
		List<Cidade> list = service.find(id).getCidades();
		List<CidadeDTO> listDto = list.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listDto);
	}
	
	//paginação
//	@GetMapping(value = "/page")
//	public ResponseEntity<Page<EstadoDTO>> findPage(
//			@RequestParam(value = "page", defaultValue = "0") Integer page, 
//			@RequestParam(value = "linesPerPage", defaultValue = "24")Integer linesPerPage, 
//			@RequestParam(value = "orderBy", defaultValue = "nome")String orderBy, 
//			@RequestParam(value = "direction", defaultValue = "ASC")String direction){
//		Page<Estado> list = service.findPage(page, linesPerPage, orderBy, direction);
//		Page<EstadoDTO> listDto = list.map(obj -> new EstadoDTO(obj));
//		return ResponseEntity.ok().body(listDto);
//	}
	
//	@PreAuthorize("hasAnyRole('ADMIN')")
//	@PostMapping
//	public ResponseEntity<Void> insert(@Valid @RequestBody EstadoDTO objDto){
//		Estado obj = service.fromDTO(objDto);
//		obj = service.insert(obj);
//		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
//				.path("/{id}").buildAndExpand(obj.getId()).toUri();
//		return ResponseEntity.created(uri).build();
//	}
//	
//	@PreAuthorize("hasAnyRole('ADMIN')")
//	@PutMapping(value = "/{id}")
//	public ResponseEntity<Void> update(@Valid @RequestBody EstadoDTO objDto, @PathVariable Integer id){
//		Estado obj = service.fromDTO(objDto);
//		obj.setId(id);
//		obj = service.update(obj);
//		return ResponseEntity.noContent().build();
//	}
//	
//	@PreAuthorize("hasAnyRole('ADMIN')")
//	@DeleteMapping (value = "/{id}")
//	public ResponseEntity<Void> delete(@PathVariable Integer id) {
//		service.delete(id);
//		return ResponseEntity.noContent().build();
//	}
}
