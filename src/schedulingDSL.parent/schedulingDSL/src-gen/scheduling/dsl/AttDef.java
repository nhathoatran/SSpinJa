/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import java.lang.String;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Att Def</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.AttDef#getName <em>Name</em>}</li>
 *   <li>{@link scheduling.dsl.AttDef#getType <em>Type</em>}</li>
 *   <li>{@link scheduling.dsl.AttDef#getList <em>List</em>}</li>
 *   <li>{@link scheduling.dsl.AttDef#getDefault <em>Default</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getAttDef()
 * @model
 * @generated
 */
public interface AttDef extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see scheduling.dsl.DslPackage#getAttDef_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link scheduling.dsl.AttDef#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * The literals are from the enumeration {@link scheduling.dsl.String}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see scheduling.dsl.String
   * @see #setType(scheduling.dsl.String)
   * @see scheduling.dsl.DslPackage#getAttDef_Type()
   * @model
   * @generated
   */
  scheduling.dsl.String getType();

  /**
   * Sets the value of the '{@link scheduling.dsl.AttDef#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see scheduling.dsl.String
   * @see #getType()
   * @generated
   */
  void setType(scheduling.dsl.String value);

  /**
   * Returns the value of the '<em><b>List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>List</em>' containment reference.
   * @see #setList(ListDef)
   * @see scheduling.dsl.DslPackage#getAttDef_List()
   * @model containment="true"
   * @generated
   */
  ListDef getList();

  /**
   * Sets the value of the '{@link scheduling.dsl.AttDef#getList <em>List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>List</em>' containment reference.
   * @see #getList()
   * @generated
   */
  void setList(ListDef value);

  /**
   * Returns the value of the '<em><b>Default</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Default</em>' containment reference.
   * @see #setDefault(Value)
   * @see scheduling.dsl.DslPackage#getAttDef_Default()
   * @model containment="true"
   * @generated
   */
  Value getDefault();

  /**
   * Sets the value of the '{@link scheduling.dsl.AttDef#getDefault <em>Default</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Default</em>' containment reference.
   * @see #getDefault()
   * @generated
   */
  void setDefault(Value value);

} // AttDef
