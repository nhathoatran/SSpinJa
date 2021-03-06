/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import java.lang.String;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assign Parameters</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.AssignParameters#getParaname <em>Paraname</em>}</li>
 *   <li>{@link scheduling.dsl.AssignParameters#getList <em>List</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getAssignParameters()
 * @model
 * @generated
 */
public interface AssignParameters extends EObject
{
  /**
   * Returns the value of the '<em><b>Paraname</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Paraname</em>' attribute.
   * @see #setParaname(String)
   * @see scheduling.dsl.DslPackage#getAssignParameters_Paraname()
   * @model
   * @generated
   */
  String getParaname();

  /**
   * Sets the value of the '{@link scheduling.dsl.AssignParameters#getParaname <em>Paraname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Paraname</em>' attribute.
   * @see #getParaname()
   * @generated
   */
  void setParaname(String value);

  /**
   * Returns the value of the '<em><b>List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>List</em>' containment reference.
   * @see #setList(ListDef)
   * @see scheduling.dsl.DslPackage#getAssignParameters_List()
   * @model containment="true"
   * @generated
   */
  ListDef getList();

  /**
   * Sets the value of the '{@link scheduling.dsl.AssignParameters#getList <em>List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>List</em>' containment reference.
   * @see #getList()
   * @generated
   */
  void setList(ListDef value);

} // AssignParameters
