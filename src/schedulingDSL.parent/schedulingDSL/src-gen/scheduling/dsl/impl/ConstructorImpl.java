/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl.impl;

import java.lang.String;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import scheduling.dsl.Constructor;
import scheduling.dsl.DslPackage;
import scheduling.dsl.ParameterList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Constructor</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.impl.ConstructorImpl#getProcessname <em>Processname</em>}</li>
 *   <li>{@link scheduling.dsl.impl.ConstructorImpl#getParalist <em>Paralist</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConstructorImpl extends MinimalEObjectImpl.Container implements Constructor
{
  /**
   * The default value of the '{@link #getProcessname() <em>Processname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProcessname()
   * @generated
   * @ordered
   */
  protected static final String PROCESSNAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getProcessname() <em>Processname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProcessname()
   * @generated
   * @ordered
   */
  protected String processname = PROCESSNAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getParalist() <em>Paralist</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParalist()
   * @generated
   * @ordered
   */
  protected ParameterList paralist;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ConstructorImpl()
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
    return DslPackage.eINSTANCE.getConstructor();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getProcessname()
  {
    return processname;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setProcessname(String newProcessname)
  {
    String oldProcessname = processname;
    processname = newProcessname;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.CONSTRUCTOR__PROCESSNAME, oldProcessname, processname));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ParameterList getParalist()
  {
    return paralist;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetParalist(ParameterList newParalist, NotificationChain msgs)
  {
    ParameterList oldParalist = paralist;
    paralist = newParalist;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.CONSTRUCTOR__PARALIST, oldParalist, newParalist);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setParalist(ParameterList newParalist)
  {
    if (newParalist != paralist)
    {
      NotificationChain msgs = null;
      if (paralist != null)
        msgs = ((InternalEObject)paralist).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.CONSTRUCTOR__PARALIST, null, msgs);
      if (newParalist != null)
        msgs = ((InternalEObject)newParalist).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.CONSTRUCTOR__PARALIST, null, msgs);
      msgs = basicSetParalist(newParalist, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.CONSTRUCTOR__PARALIST, newParalist, newParalist));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case DslPackage.CONSTRUCTOR__PARALIST:
        return basicSetParalist(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
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
      case DslPackage.CONSTRUCTOR__PROCESSNAME:
        return getProcessname();
      case DslPackage.CONSTRUCTOR__PARALIST:
        return getParalist();
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
      case DslPackage.CONSTRUCTOR__PROCESSNAME:
        setProcessname((String)newValue);
        return;
      case DslPackage.CONSTRUCTOR__PARALIST:
        setParalist((ParameterList)newValue);
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
      case DslPackage.CONSTRUCTOR__PROCESSNAME:
        setProcessname(PROCESSNAME_EDEFAULT);
        return;
      case DslPackage.CONSTRUCTOR__PARALIST:
        setParalist((ParameterList)null);
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
      case DslPackage.CONSTRUCTOR__PROCESSNAME:
        return PROCESSNAME_EDEFAULT == null ? processname != null : !PROCESSNAME_EDEFAULT.equals(processname);
      case DslPackage.CONSTRUCTOR__PARALIST:
        return paralist != null;
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
    result.append(" (processname: ");
    result.append(processname);
    result.append(')');
    return result.toString();
  }

} //ConstructorImpl
