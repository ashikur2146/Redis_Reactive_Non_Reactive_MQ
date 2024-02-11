package reactive.email;

public interface EmailService<K, V> {
	public V sendEmail(K k) throws Exception;
	public V sendEmailSync(K k) throws Exception;
}