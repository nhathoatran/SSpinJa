/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import java.lang.String;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Array Dec</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.ArrayDec#getType <em>Type</em>}</li>
 *   <li>{@link scheduling.dsl.ArrayDec#getAName <em>AName</em>}</li>
 *   <li>{@link scheduling.dsl.ArrayDec#getNum <em>Num</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getArrayDec()
 * @model
 * @generated
 */
public interface ArrayDec extends EObject
{
  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * The literals are from the enumeration {@link scheduling.dsl.TypeName}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see scheduling.dsl.TypeName
   * @see #setType(TypeName)
   * @see scheduling.dsl.DslPackage#getArrayDec_Type()
   * @model
   * @generated
   */
  TypeName getType();

  /**
   * Sets the value of the '{@link scheduling.dsl.ArrayDec#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see scheduling.dsl.TypeName
   * @see #getType()
   * @generated
   */
  void setType(TypeName value);

  /**
   * Returns the value of the '<em><b>AName</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>AName</em>' attribute.
   * @see #setAName(String)
   * @see scheduling.dsl.DslPackage#getArrayDec_AName()
   * @model
   * @generated
   */
  String getAName();

  /**
   * Sets the value of the '{@link scheduling.dsl.ArrayDec#getAName <em>AName</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>AName</em>' attribute.
   * @see #getAName()
   * @generated
   */
  void setAName(String value);

  /**
   * Returns the value of the '<em><b>Num</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Num</em>' containment reference.
   * @see #setNum(NumValue)
   * @see scheduling.dsl.DslPackage#getArrayDec_Num()
   * @model containment="true"
   * @generated
   */
  NumValue getNum();

  /**
   * Sets the value of the '{@link scheduling.dsl.ArrayDec#getNum <em>Num</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Num</em>' containment reference.
   * @see #getNum()
   * @generated
   */
  void setNum(NumValue value);

} // ArrayDec
