/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IFs</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.IFs#getOption <em>Option</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getIFs()
 * @model
 * @generated
 */
public interface IFs extends Action
{
  /**
   * Returns the value of the '<em><b>Option</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Option</em>' containment reference.
   * @see #setOption(Options)
   * @see scheduling.dsl.DslPackage#getIFs_Option()
   * @model containment="true"
   * @generated
   */
  Options getOption();

  /**
   * Sets the value of the '{@link scheduling.dsl.IFs#getOption <em>Option</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Option</em>' containment reference.
   * @see #getOption()
   * @generated
   */
  void setOption(Options value);

} // IFs
