package com.oddlabs.regclient;

import com.oddlabs.event.Deterministic;
import com.oddlabs.http.HttpRequestParameters;
import com.oddlabs.net.TaskThread;
import com.oddlabs.registration.RegistrationInfo;
import com.oddlabs.registration.RegistrationKey;
import com.oddlabs.registration.RegistrationKeyFormatException;
import com.oddlabs.util.DeterministicSerializer;
import com.oddlabs.util.DeterministicSerializerLoopbackInterface;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.security.SignedObject;

public strictfp class RegistrationClient {
	public final static int KEY_FORMAT_EXCEPTION = 1;
	public final static int LOAD_FAILED_EXCEPTION = 2;
	public final static int HTTP_EXCEPTION = 3;

	public final static int CLIENT_TYPE_OFFLINE = 1;
	public final static int CLIENT_TYPE_ONLINE = 2;
	public final static int CLIENT_TYPE_FOREIGN = 3;
	
	private final File registration_file;
	private final HttpRequestParameters parameters;
	private final int client_type;
	private final TaskThread task_thread;

	private String potential_key;

	private final boolean registered_offline = true;
	private SignedObject signed_registration_key;
	private RegistrationInfo registration_info;
	
	private RegistrationListener registration_listener;

	protected RegistrationClient(TaskThread task_thread) {
		this(task_thread, null, null, -1);
	}
		
	public RegistrationClient(TaskThread task_thread, File registration_file, HttpRequestParameters parameters, int client_type) {
		this.task_thread = task_thread;
		this.registration_file = registration_file;
		this.parameters = parameters;
		this.client_type = client_type;

		loadRegistrationFileDeterministic();
	}

	protected final Deterministic getDeterministic() {
		return task_thread.getDeterministic();
	}

	public final void setKey(String key) {
		this.potential_key = key;
		if (client_type == CLIENT_TYPE_OFFLINE && offlineCheck())
			offlineSucceeded();
	}

	private final void loadRegistrationFileDeterministic() {
		DeterministicSerializer.load(getDeterministic(), registration_file, new DeterministicSerializerLoopbackInterface() {
			public final void failed(Exception e) {
				if (registration_listener != null)
					registration_listener.registrationFailed(LOAD_FAILED_EXCEPTION, e);
			}

			public final void loadSucceeded(Object obj) {
				try {
					SignedObject signed_obj = (SignedObject)obj;
					PublicKey public_key = RegistrationKey.loadPublicKey();
					/* Team Penguin */
					System.out.println("registration_info: " + ReflectionToStringBuilder.toString(signed_obj.getObject()));
					// Comment the next line to enable any old registration file.
					if (RegistrationKey.verify(public_key, signed_obj)) {
						signed_registration_key = signed_obj;
						registration_info = (RegistrationInfo)signed_registration_key.getObject();
						if (registration_listener != null)
							registration_listener.registrationCompleted();
					}
					// Comment the '}' character above to enable any old registration file.
					/* End Penguin */
				} catch (Exception e) {
					failed(e);
				}
			}

			public final void saveSucceeded() {
				//NOP
			}
		});
	}

	private final void offlineSucceeded() {
		if (registration_listener != null)
			registration_listener.registrationCompleted();
	}

	public final void setListener(RegistrationListener registration_listener) {
		this.registration_listener = registration_listener;
	}

	public final void register(boolean online_registering) {
	}

	private final boolean offlineCheck() {
		try {
			RegistrationKey.decode(potential_key);
			return true;
		} catch (RegistrationKeyFormatException e) {
			if (registration_listener != null)
				registration_listener.registrationFailed(KEY_FORMAT_EXCEPTION, e);
			return false;
		}
	}

	public final String getPotentialKey() {
		return potential_key;
	}
	
	public final void cancelRegistration() {
	}

	public boolean isRegistered() {
		return registration_info != null || registered_offline;
	}

	public final String getRegKey() {
		try {
			return ((RegistrationInfo)signed_registration_key.getObject()).getRegKey();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public final SignedObject getSignedRegistrationKey() {
		return signed_registration_key;
	}

	public final RegistrationInfo getRegistrationInfo() {
		return registration_info;
	}
}
