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

import scheduling.dsl.DslPackage;
import scheduling.dsl.ProcessPropertyDef;
import scheduling.dsl.ProcessPropertyName;
import scheduling.dsl.Value;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Process Property Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.impl.ProcessPropertyDefImpl#isVar <em>Var</em>}</li>
 *   <li>{@link scheduling.dsl.impl.ProcessPropertyDefImpl#isVal <em>Val</em>}</li>
 *   <li>{@link scheduling.dsl.impl.ProcessPropertyDefImpl#getType <em>Type</em>}</li>
 *   <li>{@link scheduling.dsl.impl.ProcessPropertyDefImpl#getName <em>Name</em>}</li>
 *   <li>{@link scheduling.dsl.impl.ProcessPropertyDefImpl#getPvalue <em>Pvalue</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProcessPropertyDefImpl extends MinimalEObjectImpl.Container implements ProcessPropertyDef
{
  /**
   * The default value of the '{@link #isVar() <em>Var</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isVar()
   * @generated
   * @ordered
   */
  protected static final boolean VAR_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isVar() <em>Var</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isVar()
   * @generated
   * @ordered
   */
  protected boolean var = VAR_EDEFAULT;

  /**
   * The default value of the '{@link #isVal() <em>Val</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isVal()
   * @generated
   * @ordered
   */
  protected static final boolean VAL_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isVal() <em>Val</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isVal()
   * @generated
   * @ordered
   */
  protected boolean val = VAL_EDEFAULT;

  /**
   * The default value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected static final scheduling.dsl.String TYPE_EDEFAULT = scheduling.dsl.String.TEMP;

  /**
   * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected scheduling.dsl.String type = TYPE_EDEFAULT;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected EList<ProcessPropertyName> name;

  /**
   * The cached value of the '{@link #getPvalue() <em>Pvalue</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPvalue()
   * @generated
   * @ordered
   */
  protected Value pvalue;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProcessPropertyDefImpl()
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
    return DslPackage.eINSTANCE.getProcessPropertyDef();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isVar()
  {
    return var;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setVar(boolean newVar)
  {
    boolean oldVar = var;
    var = newVar;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.PROCESS_PROPERTY_DEF__VAR, oldVar, var));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isVal()
  {
    return val;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setVal(boolean newVal)
  {
    boolean oldVal = val;
    val = newVal;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.PROCESS_PROPERTY_DEF__VAL, oldVal, val));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public scheduling.dsl.String getType()
  {
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setType(scheduling.dsl.String newType)
  {
    scheduling.dsl.String oldType = type;
    type = newType == null ? TYPE_EDEFAULT : newType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.PROCESS_PROPERTY_DEF__TYPE, oldType, type));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<ProcessPropertyName> getName()
  {
    if (name == null)
    {
      name = new EObjectContainmentEList<ProcessPropertyName>(ProcessPropertyName.class, this, DslPackage.PROCESS_PROPERTY_DEF__NAME);
    }
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Value getPvalue()
  {
    return pvalue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetPvalue(Value newPvalue, NotificationChain msgs)
  {
    Value oldPvalue = pvalue;
    pvalue = newPvalue;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.PROCESS_PROPERTY_DEF__PVALUE, oldPvalue, newPvalue);
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
  public void setPvalue(Value newPvalue)
  {
    if (newPvalue != pvalue)
    {
      NotificationChain msgs = null;
      if (pvalue != null)
        msgs = ((InternalEObject)pvalue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.PROCESS_PROPERTY_DEF__PVALUE, null, msgs);
      if (newPvalue != null)
        msgs = ((InternalEObject)newPvalue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.PROCESS_PROPERTY_DEF__PVALUE, null, msgs);
      msgs = basicSetPvalue(newPvalue, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.PROCESS_PROPERTY_DEF__PVALUE, newPvalue, newPvalue));
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
      case DslPackage.PROCESS_PROPERTY_DEF__NAME:
        return ((InternalEList<?>)getName()).basicRemove(otherEnd, msgs);
      case DslPackage.PROCESS_PROPERTY_DEF__PVALUE:
        return basicSetPvalue(null, msgs);
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
      case DslPackage.PROCESS_PROPERTY_DEF__VAR:
        return isVar();
      case DslPackage.PROCESS_PROPERTY_DEF__VAL:
        return isVal();
      case DslPackage.PROCESS_PROPERTY_DEF__TYPE:
        return getType();
      case DslPackage.PROCESS_PROPERTY_DEF__NAME:
        return getName();
      case DslPackage.PROCESS_PROPERTY_DEF__PVALUE:
        return getPvalue();
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
      case DslPackage.PROCESS_PROPERTY_DEF__VAR:
        setVar((Boolean)newValue);
        return;
      case DslPackage.PROCESS_PROPERTY_DEF__VAL:
        setVal((Boolean)newValue);
        return;
      case DslPackage.PROCESS_PROPERTY_DEF__TYPE:
        setType((scheduling.dsl.String)newValue);
        return;
      case DslPackage.PROCESS_PROPERTY_DEF__NAME:
        getName().clear();
        getName().addAll((Collection<? extends ProcessPropertyName>)newValue);
        return;
      case DslPackage.PROCESS_PROPERTY_DEF__PVALUE:
        setPvalue((Value)newValue);
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
      case DslPackage.PROCESS_PROPERTY_DEF__VAR:
        setVar(VAR_EDEFAULT);
        return;
      case DslPackage.PROCESS_PROPERTY_DEF__VAL:
        setVal(VAL_EDEFAULT);
        return;
      case DslPackage.PROCESS_PROPERTY_DEF__TYPE:
        setType(TYPE_EDEFAULT);
        return;
      case DslPackage.PROCESS_PROPERTY_DEF__NAME:
        getName().clear();
        return;
      case DslPackage.PROCESS_PROPERTY_DEF__PVALUE:
        setPvalue((Value)null);
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
      case DslPackage.PROCESS_PROPERTY_DEF__VAR:
        return var != VAR_EDEFAULT;
      case DslPackage.PROCESS_PROPERTY_DEF__VAL:
        return val != VAL_EDEFAULT;
      case DslPackage.PROCESS_PROPERTY_DEF__TYPE:
        return type != TYPE_EDEFAULT;
      case DslPackage.PROCESS_PROPERTY_DEF__NAME:
        return name != null && !name.isEmpty();
      case DslPackage.PROCESS_PROPERTY_DEF__PVALUE:
        return pvalue != null;
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
    result.append(" (var: ");
    result.append(var);
    result.append(", val: ");
    result.append(val);
    result.append(", type: ");
    result.append(type);
    result.append(')');
    return result.toString();
  }

} //ProcessPropertyDefImpl
