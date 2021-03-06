/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import java.lang.String;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Exist PID</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.ExistPID#getId <em>Id</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getExistPID()
 * @model
 * @generated
 */
public interface ExistPID extends Expression
{
  /**
   * Returns the value of the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Id</em>' attribute.
   * @see #setId(String)
   * @see scheduling.dsl.DslPackage#getExistPID_Id()
   * @model
   * @generated
   */
  String getId();

  /**
   * Sets the value of the '{@link scheduling.dsl.ExistPID#getId <em>Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Id</em>' attribute.
   * @see #getId()
   * @generated
   */
  void setId(String value);

} // ExistPID
