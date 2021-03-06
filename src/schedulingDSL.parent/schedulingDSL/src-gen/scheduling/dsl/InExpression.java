/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import java.lang.String;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>In Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.InExpression#getCol <em>Col</em>}</li>
 *   <li>{@link scheduling.dsl.InExpression#getPN <em>PN</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getInExpression()
 * @model
 * @generated
 */
public interface InExpression extends Expression
{
  /**
   * Returns the value of the '<em><b>Col</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Col</em>' containment reference.
   * @see #setCol(SchedulerSet)
   * @see scheduling.dsl.DslPackage#getInExpression_Col()
   * @model containment="true"
   * @generated
   */
  SchedulerSet getCol();

  /**
   * Sets the value of the '{@link scheduling.dsl.InExpression#getCol <em>Col</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Col</em>' containment reference.
   * @see #getCol()
   * @generated
   */
  void setCol(SchedulerSet value);

  /**
   * Returns the value of the '<em><b>PN</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>PN</em>' attribute.
   * @see #setPN(String)
   * @see scheduling.dsl.DslPackage#getInExpression_PN()
   * @model
   * @generated
   */
  String getPN();

  /**
   * Sets the value of the '{@link scheduling.dsl.InExpression#getPN <em>PN</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>PN</em>' attribute.
   * @see #getPN()
   * @generated
   */
  void setPN(String value);

} // InExpression
