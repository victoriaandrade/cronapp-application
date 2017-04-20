package diagram.social.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;
import diagram.social.dao.*;
import diagram.social.entity.*;
import cloud.CloudManager;

/**
 * Classe que representa a camada de negócios de PerfilBusiness
 * 
 * @generated
 **/
@Service("PerfilBusiness")
public class PerfilBusiness {


  private static final String DROPBOX_APP_ACCESS_TOKEN = "34Nq6-ZLM8YAAAAAAAAQSPnHb18KnHL7PnXZ6SJjtfmxxfw7HVoz9xFAjkeLQnXp";

  private final CloudManager cloudManager = CloudManager.newInstance().byID("id").toFields("foto");

  /**
   * Instância da classe PerfilDAO que faz o acesso ao banco de dados
   * 
   * @generated
   */
  @Autowired
  @Qualifier("PerfilDAO")
  protected PerfilDAO repository;

  // CRUD

  /**
   * Serviço exposto para novo registro de acordo com a entidade fornecida
   * 
   * @generated
   */
  public Perfil post(final Perfil entity) throws Exception {
    // begin-user-code  
    // end-user-code  
    Perfil result = null;
    byte[] foto = entity.getFoto();
    entity.setFoto(null);
    result = repository.save(entity);
    result.setFoto(foto);
    this.cloudManager.byEntity(result).build().dropbox(DROPBOX_APP_ACCESS_TOKEN).upload();    // begin-user-code
    // end-user-code
    return result;
  }

  /**
   * Serviço exposto para salvar alterações de acordo com a entidade fornecida
   * 
   * @generated
   */
  public Perfil put(final Perfil entity) throws Exception {
    // begin-user-code  
    // end-user-code
    Perfil result = null;
    byte[] foto = entity.getFoto();

    entity.setFoto(null);
    result = repository.saveAndFlush(entity);
    result.setFoto(foto);
    this.cloudManager.byEntity(result).build().dropbox(DROPBOX_APP_ACCESS_TOKEN).upload();
    // begin-user-code
    // end-user-code
    return result;
  }

  /**
   * Serviço exposto para remover a entidade de acordo com o id fornecido
   * 
   * @generated
   */
  public void delete(java.lang.String id) throws Exception {
    Perfil entity = this.get(id);
    // begin-user-code  
    // end-user-code
    this.repository.delete(entity);
      this.cloudManager.byEntity(entity).build().dropbox(DROPBOX_APP_ACCESS_TOKEN).delete();
    // begin-user-code  
    // end-user-code        
  }
  
  /**
   * Serviço exposto para recuperar a entidade de acordo com o id fornecido
   * 
   * @generated
   */
  public Perfil get(java.lang.String id) throws Exception {
    // begin-user-code  
    // end-user-code
    Perfil result = repository.findOne(id);
    this.cloudManager.byEntity(result).build().dropbox(DROPBOX_APP_ACCESS_TOKEN).popule(result);
    // begin-user-code
    // end-user-code
    return result;
  }

  // CRUD
  
  /**
   * Lista com paginação de acordo com a NamedQuery
   * 
   * @generated
   */
  public Page<Perfil> list(Pageable pageable){
    // begin-user-code
    // end-user-code
    Page<Perfil> result = repository.list(pageable);
    result.forEach(item -> cloudManager.byEntity(item).build().dropbox(DROPBOX_APP_ACCESS_TOKEN).popule(item));
    // begin-user-code
    // end-user-code
    return result;
  }
}
