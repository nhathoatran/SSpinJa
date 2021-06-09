/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import java.lang.String;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Exist Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.ExistExpression#getPN <em>PN</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getExistExpression()
 * @model
 * @generated
 */
public interface ExistExpression extends Expression
{
  /**
   * Returns the value of the '<em><b>PN</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>PN</em>' attribute.
   * @see #setPN(String)
   * @see scheduling.dsl.DslPackage#getExistExpression_PN()
   * @model
   * @generated
   */
  String getPN();

  /**
   * Sets the value of the '{@link scheduling.dsl.ExistExpression#getPN <em>PN</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>PN</em>' attribute.
   * @see #getPN()
   * @generated
   */
  void setPN(String value);

} // ExistExpression
