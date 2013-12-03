/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\android\\code\\Guild_War2_Event_Tracker\\src\\tw\\yume190\\guild_war2_event_tracker\\karka2\\KarkaAIDL.aidl
 */
package tw.yume190.guild_war2_event_tracker.karka2;
public interface KarkaAIDL extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements tw.yume190.guild_war2_event_tracker.karka2.KarkaAIDL
{
private static final java.lang.String DESCRIPTOR = "tw.yume190.guild_war2_event_tracker.karka2.KarkaAIDL";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an tw.yume190.guild_war2_event_tracker.karka2.KarkaAIDL interface,
 * generating a proxy if needed.
 */
public static tw.yume190.guild_war2_event_tracker.karka2.KarkaAIDL asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof tw.yume190.guild_war2_event_tracker.karka2.KarkaAIDL))) {
return ((tw.yume190.guild_war2_event_tracker.karka2.KarkaAIDL)iin);
}
return new tw.yume190.guild_war2_event_tracker.karka2.KarkaAIDL.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_get:
{
data.enforceInterface(DESCRIPTOR);
java.util.Map _result = this.get();
reply.writeNoException();
reply.writeMap(_result);
return true;
}
case TRANSACTION_update:
{
data.enforceInterface(DESCRIPTOR);
this.update();
reply.writeNoException();
return true;
}
case TRANSACTION_getUpdateState:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getUpdateState();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements tw.yume190.guild_war2_event_tracker.karka2.KarkaAIDL
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.util.Map get() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.Map _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_get, _data, _reply, 0);
_reply.readException();
java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
_result = _reply.readHashMap(cl);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void update() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_update, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean getUpdateState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getUpdateState, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_get = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_update = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getUpdateState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public java.util.Map get() throws android.os.RemoteException;
public void update() throws android.os.RemoteException;
public boolean getUpdateState() throws android.os.RemoteException;
}
