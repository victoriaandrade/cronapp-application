package diagram.social.dao;

import diagram.social.entity.*;
import java.util.*;
import org.springframework.stereotype.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.*;
import org.springframework.transaction.annotation.*; 

/**
 * Realiza operação de Create, Read, Update e Delete no banco de dados.
 * Os métodos de create, edit, delete e outros estão abstraídos no JpaRepository
 * 
 * @see org.springframework.data.jpa.repository.JpaRepository
 * 
 * @generated
 */
@Repository("PerfilDAO")
@Transactional(transactionManager="diagram.social-TransactionManager")
public interface PerfilDAO extends JpaRepository<Perfil, java.lang.String> {

  /**
   * Obtém a instância de Perfil utilizando os identificadores
   * 
   * @param id
   *          Identificador 
   * @return Instância relacionada com o filtro indicado
   * @generated
   */    
  @Query("SELECT entity FROM Perfil entity WHERE entity.id = :id")
  public Perfil findOne(@Param(value="id") java.lang.String id);

  /**
   * Remove a instância de Perfil utilizando os identificadores
   * 
   * @param id
   *          Identificador 
   * @return Quantidade de modificações efetuadas
   * @generated
   */    
  @Modifying
  @Query("DELETE FROM Perfil entity WHERE entity.id = :id")
  public void delete(@Param(value="id") java.lang.String id);

  /**
   * Lista com paginação de acordo com a NamedQuery
   * 
   * @generated
   */
  @Query("select c from Perfil c")
  public Page<Perfil> list(Pageable pageable);
  


}
