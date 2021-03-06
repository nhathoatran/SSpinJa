/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Behaviors</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.ProcessBehaviors#getProcessbehavior <em>Processbehavior</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getProcessBehaviors()
 * @model
 * @generated
 */
public interface ProcessBehaviors extends EObject
{
  /**
   * Returns the value of the '<em><b>Processbehavior</b></em>' containment reference list.
   * The list contents are of type {@link scheduling.dsl.ProcessBehavior}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Processbehavior</em>' containment reference list.
   * @see scheduling.dsl.DslPackage#getProcessBehaviors_Processbehavior()
   * @model containment="true"
   * @generated
   */
  EList<ProcessBehavior> getProcessbehavior();

} // ProcessBehaviors
