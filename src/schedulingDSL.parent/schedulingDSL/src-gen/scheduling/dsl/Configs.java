/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Configs</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.Configs#getConfig <em>Config</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getConfigs()
 * @model
 * @generated
 */
public interface Configs extends EObject
{
  /**
   * Returns the value of the '<em><b>Config</b></em>' containment reference list.
   * The list contents are of type {@link scheduling.dsl.Config}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Config</em>' containment reference list.
   * @see scheduling.dsl.DslPackage#getConfigs_Config()
   * @model containment="true"
   * @generated
   */
  EList<Config> getConfig();

} // Configs
