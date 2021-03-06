/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Print Log Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.PrintLogStatement#getSt <em>St</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getPrintLogStatement()
 * @model
 * @generated
 */
public interface PrintLogStatement extends Statement
{
  /**
   * Returns the value of the '<em><b>St</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>St</em>' containment reference.
   * @see #setSt(Expression)
   * @see scheduling.dsl.DslPackage#getPrintLogStatement_St()
   * @model containment="true"
   * @generated
   */
  Expression getSt();

  /**
   * Sets the value of the '{@link scheduling.dsl.PrintLogStatement#getSt <em>St</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>St</em>' containment reference.
   * @see #getSt()
   * @generated
   */
  void setSt(Expression value);

} // PrintLogStatement
