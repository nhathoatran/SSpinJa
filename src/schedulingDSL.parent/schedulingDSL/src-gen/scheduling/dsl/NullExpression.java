/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Null Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.NullExpression#getProcName <em>Proc Name</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getNullExpression()
 * @model
 * @generated
 */
public interface NullExpression extends Expression
{
  /**
   * Returns the value of the '<em><b>Proc Name</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Proc Name</em>' containment reference.
   * @see #setProcName(scheduling.dsl.Process)
   * @see scheduling.dsl.DslPackage#getNullExpression_ProcName()
   * @model containment="true"
   * @generated
   */
  scheduling.dsl.Process getProcName();

  /**
   * Sets the value of the '{@link scheduling.dsl.NullExpression#getProcName <em>Proc Name</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Proc Name</em>' containment reference.
   * @see #getProcName()
   * @generated
   */
  void setProcName(scheduling.dsl.Process value);

} // NullExpression