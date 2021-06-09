/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scheduling.dsl.AssignParameters;
import scheduling.dsl.Constraints;
import scheduling.dsl.DslPackage;
import scheduling.dsl.FunctionName;
import scheduling.dsl.InterfaceParameterList;
import scheduling.dsl.Method;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Method</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.impl.MethodImpl#getFunctionname <em>Functionname</em>}</li>
 *   <li>{@link scheduling.dsl.impl.MethodImpl#getParameterlist <em>Parameterlist</em>}</li>
 *   <li>{@link scheduling.dsl.impl.MethodImpl#getAssignparameters <em>Assignparameters</em>}</li>
 *   <li>{@link scheduling.dsl.impl.MethodImpl#getConstraints <em>Constraints</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MethodImpl extends MinimalEObjectImpl.Container implements Method
{
  /**
   * The cached value of the '{@link #getFunctionname() <em>Functionname</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFunctionname()
   * @generated
   * @ordered
   */
  protected FunctionName functionname;

  /**
   * The cached value of the '{@link #getParameterlist() <em>Parameterlist</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParameterlist()
   * @generated
   * @ordered
   */
  protected InterfaceParameterList parameterlist;

  /**
   * The cached value of the '{@link #getAssignparameters() <em>Assignparameters</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAssignparameters()
   * @generated
   * @ordered
   */
  protected EList<AssignParameters> assignparameters;

  /**
   * The cached value of the '{@link #getConstraints() <em>Constraints</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConstraints()
   * @generated
   * @ordered
   */
  protected Constraints constraints;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MethodImpl()
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
    return DslPackage.eINSTANCE.getMethod();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public FunctionName getFunctionname()
  {
    return functionname;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetFunctionname(FunctionName newFunctionname, NotificationChain msgs)
  {
    FunctionName oldFunctionname = functionname;
    functionname = newFunctionname;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.METHOD__FUNCTIONNAME, oldFunctionname, newFunctionname);
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
  public void setFunctionname(FunctionName newFunctionname)
  {
    if (newFunctionname != functionname)
    {
      NotificationChain msgs = null;
      if (functionname != null)
        msgs = ((InternalEObject)functionname).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.METHOD__FUNCTIONNAME, null, msgs);
      if (newFunctionname != null)
        msgs = ((InternalEObject)newFunctionname).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.METHOD__FUNCTIONNAME, null, msgs);
      msgs = basicSetFunctionname(newFunctionname, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.METHOD__FUNCTIONNAME, newFunctionname, newFunctionname));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public InterfaceParameterList getParameterlist()
  {
    return parameterlist;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetParameterlist(InterfaceParameterList newParameterlist, NotificationChain msgs)
  {
    InterfaceParameterList oldParameterlist = parameterlist;
    parameterlist = newParameterlist;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.METHOD__PARAMETERLIST, oldParameterlist, newParameterlist);
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
  public void setParameterlist(InterfaceParameterList newParameterlist)
  {
    if (newParameterlist != parameterlist)
    {
      NotificationChain msgs = null;
      if (parameterlist != null)
        msgs = ((InternalEObject)parameterlist).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.METHOD__PARAMETERLIST, null, msgs);
      if (newParameterlist != null)
        msgs = ((InternalEObject)newParameterlist).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.METHOD__PARAMETERLIST, null, msgs);
      msgs = basicSetParameterlist(newParameterlist, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.METHOD__PARAMETERLIST, newParameterlist, newParameterlist));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<AssignParameters> getAssignparameters()
  {
    if (assignparameters == null)
    {
      assignparameters = new EObjectContainmentEList<AssignParameters>(AssignParameters.class, this, DslPackage.METHOD__ASSIGNPARAMETERS);
    }
    return assignparameters;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Constraints getConstraints()
  {
    return constraints;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetConstraints(Constraints newConstraints, NotificationChain msgs)
  {
    Constraints oldConstraints = constraints;
    constraints = newConstraints;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.METHOD__CONSTRAINTS, oldConstraints, newConstraints);
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
  public void setConstraints(Constraints newConstraints)
  {
    if (newConstraints != constraints)
    {
      NotificationChain msgs = null;
      if (constraints != null)
        msgs = ((InternalEObject)constraints).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.METHOD__CONSTRAINTS, null, msgs);
      if (newConstraints != null)
        msgs = ((InternalEObject)newConstraints).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.METHOD__CONSTRAINTS, null, msgs);
      msgs = basicSetConstraints(newConstraints, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.METHOD__CONSTRAINTS, newConstraints, newConstraints));
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
      case DslPackage.METHOD__FUNCTIONNAME:
        return basicSetFunctionname(null, msgs);
      case DslPackage.METHOD__PARAMETERLIST:
        return basicSetParameterlist(null, msgs);
      case DslPackage.METHOD__ASSIGNPARAMETERS:
        return ((InternalEList<?>)getAssignparameters()).basicRemove(otherEnd, msgs);
      case DslPackage.METHOD__CONSTRAINTS:
        return basicSetConstraints(null, msgs);
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
      case DslPackage.METHOD__FUNCTIONNAME:
        return getFunctionname();
      case DslPackage.METHOD__PARAMETERLIST:
        return getParameterlist();
      case DslPackage.METHOD__ASSIGNPARAMETERS:
        return getAssignparameters();
      case DslPackage.METHOD__CONSTRAINTS:
        return getConstraints();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case DslPackage.METHOD__FUNCTIONNAME:
        setFunctionname((FunctionName)newValue);
        return;
      case DslPackage.METHOD__PARAMETERLIST:
        setParameterlist((InterfaceParameterList)newValue);
        return;
      case DslPackage.METHOD__ASSIGNPARAMETERS:
        getAssignparameters().clear();
        getAssignparameters().addAll((Collection<? extends AssignParameters>)newValue);
        return;
      case DslPackage.METHOD__CONSTRAINTS:
        setConstraints((Constraints)newValue);
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
      case DslPackage.METHOD__FUNCTIONNAME:
        setFunctionname((FunctionName)null);
        return;
      case DslPackage.METHOD__PARAMETERLIST:
        setParameterlist((InterfaceParameterList)null);
        return;
      case DslPackage.METHOD__ASSIGNPARAMETERS:
        getAssignparameters().clear();
        return;
      case DslPackage.METHOD__CONSTRAINTS:
        setConstraints((Constraints)null);
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
      case DslPackage.METHOD__FUNCTIONNAME:
        return functionname != null;
      case DslPackage.METHOD__PARAMETERLIST:
        return parameterlist != null;
      case DslPackage.METHOD__ASSIGNPARAMETERS:
        return assignparameters != null && !assignparameters.isEmpty();
      case DslPackage.METHOD__CONSTRAINTS:
        return constraints != null;
    }
    return super.eIsSet(featureID);
  }

} //MethodImpl