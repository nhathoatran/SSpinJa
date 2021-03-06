/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Execute Process</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.ExecuteProcess#getProcess <em>Process</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getExecuteProcess()
 * @model
 * @generated
 */
public interface ExecuteProcess extends EObject
{
  /**
   * Returns the value of the '<em><b>Process</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Process</em>' containment reference.
   * @see #setProcess(scheduling.dsl.Process)
   * @see scheduling.dsl.DslPackage#getExecuteProcess_Process()
   * @model containment="true"
   * @generated
   */
  scheduling.dsl.Process getProcess();

  /**
   * Sets the value of the '{@link scheduling.dsl.ExecuteProcess#getProcess <em>Process</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Process</em>' containment reference.
   * @see #getProcess()
   * @generated
   */
  void setProcess(scheduling.dsl.Process value);

} // ExecuteProcess
