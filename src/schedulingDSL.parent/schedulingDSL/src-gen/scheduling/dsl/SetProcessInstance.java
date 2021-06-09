/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Set Process Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.SetProcessInstance#getProcess <em>Process</em>}</li>
 *   <li>{@link scheduling.dsl.SetProcessInstance#getColTo <em>Col To</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getSetProcessInstance()
 * @model
 * @generated
 */
public interface SetProcessInstance extends Statement
{
  /**
   * Returns the value of the '<em><b>Process</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Process</em>' containment reference.
   * @see #setProcess(scheduling.dsl.Process)
   * @see scheduling.dsl.DslPackage#getSetProcessInstance_Process()
   * @model containment="true"
   * @generated
   */
  scheduling.dsl.Process getProcess();

  /**
   * Sets the value of the '{@link scheduling.dsl.SetProcessInstance#getProcess <em>Process</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Process</em>' containment reference.
   * @see #getProcess()
   * @generated
   */
  void setProcess(scheduling.dsl.Process value);

  /**
   * Returns the value of the '<em><b>Col To</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Col To</em>' containment reference.
   * @see #setColTo(SchedulerSet)
   * @see scheduling.dsl.DslPackage#getSetProcessInstance_ColTo()
   * @model containment="true"
   * @generated
   */
  SchedulerSet getColTo();

  /**
   * Sets the value of the '{@link scheduling.dsl.SetProcessInstance#getColTo <em>Col To</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Col To</em>' containment reference.
   * @see #getColTo()
   * @generated
   */
  void setColTo(SchedulerSet value);

} // SetProcessInstance
