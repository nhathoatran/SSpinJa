/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl.impl;

import java.lang.String;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scheduling.dsl.DslPackage;
import scheduling.dsl.ProcessName;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Process Name</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.impl.ProcessNameImpl#getPName <em>PName</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProcessNameImpl extends AtomicImpl implements ProcessName
{
  /**
   * The default value of the '{@link #getPName() <em>PName</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPName()
   * @generated
   * @ordered
   */
  protected static final String PNAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPName() <em>PName</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPName()
   * @generated
   * @ordered
   */
  protected String pName = PNAME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProcessNameImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return DslPackage.eINSTANCE.getProcessName();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getPName()
  {
    return pName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setPName(String newPName)
  {
    String oldPName = pName;
    pName = newPName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.PROCESS_NAME__PNAME, oldPName, pName));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case DslPackage.PROCESS_NAME__PNAME:
        return getPName();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case DslPackage.PROCESS_NAME__PNAME:
        setPName((String)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case DslPackage.PROCESS_NAME__PNAME:
        setPName(PNAME_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case DslPackage.PROCESS_NAME__PNAME:
        return PNAME_EDEFAULT == null ? pName != null : !PNAME_EDEFAULT.equals(pName);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (pName: ");
    result.append(pName);
    result.append(')');
    return result.toString();
  }

} //ProcessNameImpl
