package br.com.springboot.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "fornecedores")
public class Fornecedor implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	@NotBlank(message = "informe o nome")
	@Size(min = 3, max= 50)
	private String nome;
	@Column(length = 14)
	@CNPJ(message = "CNPJ inválido")
	private String cnpj;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Column(name = "data_nascimento", columnDefinition = "DATE")
	@NotNull(message = "Informe a Data de Cadastro")
	private LocalDate dataDeCadastro;
	@Column(length = 14)
	private String telefone;
	@Column(length = 15)
	private String celular;
	@Column(length = 50)
	@Email(message = "e-mail inválido")
	private String email;
	private boolean ativo;

	@Override
	public String toString() {
		return "Fornecedor{" +
				"id=" + id +
				", nome='" + nome + '\'' +
				", CNPJ='" + cnpj + '\'' +
				", dataDeCadastro=" + dataDeCadastro +
				", telefone='" + telefone + '\'' +
				", celular='" + celular + '\'' +
				", email='" + email + '\'' +
				", ativo=" + ativo +
				'}';
	}
}
