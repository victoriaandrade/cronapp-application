package diagram.social.rest;

import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.*;
import java.util.*;
import diagram.social.entity.*;
import diagram.social.business.*;

/**
 * Controller para expor serviços REST de Perfil
 * 
 * @generated
 **/
@RestController
@RequestMapping(value = "/api/rest/diagram/social/Perfil")
public class PerfilREST {

  /**
   * Classe de negócio para manipulação de dados
   * 
   * @generated
   */
  @Autowired
  @Qualifier("PerfilBusiness")
  private PerfilBusiness perfilBusiness;

  /**
   * Serviço exposto para novo registro de acordo com a entidade fornecida
   * 
   * @generated
   */
  @RequestMapping(method = RequestMethod.POST)
  public Perfil post(@Validated @RequestBody final Perfil entity) throws Exception {
    return perfilBusiness.post(entity);
  }

  /**
   * Serviço exposto para salvar alterações de acordo com a entidade fornecida
   * 
   * @generated
   */
  @RequestMapping(method = RequestMethod.PUT)
  public Perfil put(@Validated @RequestBody final Perfil entity) throws Exception {
    return perfilBusiness.put(entity);
  }

  /**
   * Serviço exposto para salvar alterações de acordo com a entidade e id fornecidos
   * 
   * @generated
   */
  @RequestMapping(method = RequestMethod.PUT, value = "/{perfilId}")
  public Perfil put(@Validated @RequestBody final Perfil entity, @PathVariable("perfilId") java.lang.String perfilId) throws Exception {
    return perfilBusiness.put(entity);
  }  

  /**
   * Serviço exposto para remover a entidade de acordo com o id fornecido
   * 
   * @generated
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/{perfilId}")
  public void delete(@PathVariable("perfilId") java.lang.String perfilId) throws Exception {
    perfilBusiness.delete(perfilId);
  }

  /**
   * NamedQuery list
   * @generated
   */
  @RequestMapping(method = RequestMethod.GET)
  public HttpEntity<PagedResources<Perfil>> listParams(Pageable pageable, PagedResourcesAssembler assembler){
    return new ResponseEntity<>(assembler.toResource(perfilBusiness.list(pageable)), HttpStatus.OK);    
  }

  /**
   * Serviço exposto para recuperar a entidade de acordo com o id fornecido
   * 
   * @generated
   */
  @RequestMapping(method = RequestMethod.GET, value = "/{perfilId}")
  public Perfil get(@PathVariable("perfilId") java.lang.String perfilId) throws Exception {
    return perfilBusiness.get(perfilId);
  }
}
