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
import scheduling.dsl.ELSEs;
import scheduling.dsl.Options;
import scheduling.dsl.SequenceAction;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Options</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.impl.OptionsImpl#getSequence <em>Sequence</em>}</li>
 *   <li>{@link scheduling.dsl.impl.OptionsImpl#getElses <em>Elses</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OptionsImpl extends MinimalEObjectImpl.Container implements Options
{
  /**
   * The cached value of the '{@link #getSequence() <em>Sequence</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSequence()
   * @generated
   * @ordered
   */
  protected EList<SequenceAction> sequence;

  /**
   * The cached value of the '{@link #getElses() <em>Elses</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getElses()
   * @generated
   * @ordered
   */
  protected ELSEs elses;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected OptionsImpl()
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
    return DslPackage.eINSTANCE.getOptions();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<SequenceAction> getSequence()
  {
    if (sequence == null)
    {
      sequence = new EObjectContainmentEList<SequenceAction>(SequenceAction.class, this, DslPackage.OPTIONS__SEQUENCE);
    }
    return sequence;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ELSEs getElses()
  {
    return elses;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetElses(ELSEs newElses, NotificationChain msgs)
  {
    ELSEs oldElses = elses;
    elses = newElses;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.OPTIONS__ELSES, oldElses, newElses);
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
  public void setElses(ELSEs newElses)
  {
    if (newElses != elses)
    {
      NotificationChain msgs = null;
      if (elses != null)
        msgs = ((InternalEObject)elses).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.OPTIONS__ELSES, null, msgs);
      if (newElses != null)
        msgs = ((InternalEObject)newElses).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.OPTIONS__ELSES, null, msgs);
      msgs = basicSetElses(newElses, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.OPTIONS__ELSES, newElses, newElses));
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
      case DslPackage.OPTIONS__SEQUENCE:
        return ((InternalEList<?>)getSequence()).basicRemove(otherEnd, msgs);
      case DslPackage.OPTIONS__ELSES:
        return basicSetElses(null, msgs);
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
      case DslPackage.OPTIONS__SEQUENCE:
        return getSequence();
      case DslPackage.OPTIONS__ELSES:
        return getElses();
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
      case DslPackage.OPTIONS__SEQUENCE:
        getSequence().clear();
        getSequence().addAll((Collection<? extends SequenceAction>)newValue);
        return;
      case DslPackage.OPTIONS__ELSES:
        setElses((ELSEs)newValue);
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
      case DslPackage.OPTIONS__SEQUENCE:
        getSequence().clear();
        return;
      case DslPackage.OPTIONS__ELSES:
        setElses((ELSEs)null);
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
      case DslPackage.OPTIONS__SEQUENCE:
        return sequence != null && !sequence.isEmpty();
      case DslPackage.OPTIONS__ELSES:
        return elses != null;
    }
    return super.eIsSet(featureID);
  }

} //OptionsImpl