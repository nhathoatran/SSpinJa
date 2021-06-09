/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Step Generation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.StepGeneration#getStep <em>Step</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getStepGeneration()
 * @model
 * @generated
 */
public interface StepGeneration extends EObject
{
  /**
   * Returns the value of the '<em><b>Step</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Step</em>' containment reference.
   * @see #setStep(Template)
   * @see scheduling.dsl.DslPackage#getStepGeneration_Step()
   * @model containment="true"
   * @generated
   */
  Template getStep();

  /**
   * Sets the value of the '{@link scheduling.dsl.StepGeneration#getStep <em>Step</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Step</em>' containment reference.
   * @see #getStep()
   * @generated
   */
  void setStep(Template value);

} // StepGeneration