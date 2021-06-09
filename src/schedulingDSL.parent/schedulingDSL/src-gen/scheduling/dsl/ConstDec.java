/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import java.lang.String;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Const Dec</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.ConstDec#getName <em>Name</em>}</li>
 *   <li>{@link scheduling.dsl.ConstDec#getBvalue <em>Bvalue</em>}</li>
 *   <li>{@link scheduling.dsl.ConstDec#getIvalue <em>Ivalue</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getConstDec()
 * @model
 * @generated
 */
public interface ConstDec extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see scheduling.dsl.DslPackage#getConstDec_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link scheduling.dsl.ConstDec#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Bvalue</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Bvalue</em>' containment reference.
   * @see #setBvalue(BoolValue)
   * @see scheduling.dsl.DslPackage#getConstDec_Bvalue()
   * @model containment="true"
   * @generated
   */
  BoolValue getBvalue();

  /**
   * Sets the value of the '{@link scheduling.dsl.ConstDec#getBvalue <em>Bvalue</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Bvalue</em>' containment reference.
   * @see #getBvalue()
   * @generated
   */
  void setBvalue(BoolValue value);

  /**
   * Returns the value of the '<em><b>Ivalue</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ivalue</em>' containment reference.
   * @see #setIvalue(NumValue)
   * @see scheduling.dsl.DslPackage#getConstDec_Ivalue()
   * @model containment="true"
   * @generated
   */
  NumValue getIvalue();

  /**
   * Sets the value of the '{@link scheduling.dsl.ConstDec#getIvalue <em>Ivalue</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Ivalue</em>' containment reference.
   * @see #getIvalue()
   * @generated
   */
  void setIvalue(NumValue value);

} // ConstDec
