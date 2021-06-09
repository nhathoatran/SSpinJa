/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Set Return Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.SetReturnSet#getCol <em>Col</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getSetReturnSet()
 * @model
 * @generated
 */
public interface SetReturnSet extends Statement
{
  /**
   * Returns the value of the '<em><b>Col</b></em>' containment reference list.
   * The list contents are of type {@link scheduling.dsl.SchedulerSet}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Col</em>' containment reference list.
   * @see scheduling.dsl.DslPackage#getSetReturnSet_Col()
   * @model containment="true"
   * @generated
   */
  EList<SchedulerSet> getCol();

} // SetReturnSet
