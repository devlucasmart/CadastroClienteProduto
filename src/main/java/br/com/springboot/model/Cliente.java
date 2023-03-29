package br.com.springboot.model;

import java.io.Serializable;
import java.time.LocalDate;

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

import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	@NotBlank(message = "informe o nome")
	@Size(min = 3, max= 50)
	private String nome;
	@Column(length = 14)
	@CPF(message = "CPF inválido")
	private String cpf;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Column(name = "data_nascimento", columnDefinition = "DATE")
	@NotNull(message = "Informe a Data de Nascimento")
	private LocalDate dataDeNascimento;
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Informe o Sexo")
	private Sexo sexo;
	@Column(length = 14)
	private String telefone;
	@Column(length = 15)
	private String celular;
	@Column(length = 50)
	@Email(message = "e-mail inválido")
	private String email;
	private boolean ativo;

	public Cliente() {
		this.ativo = true;
	}
	public Sexo getSexo(Sexo sexo) {

		return sexo;
	}

	@Override
	public String toString() {
			String cliente = "";
			cliente += "CLIENTE\n";
			cliente += "----------------------";
			cliente += "ID........." + this.id + "\n";
			cliente += "Nome........." + this.nome + "\n";
			cliente += "CPF........." + this.cpf + "\n";
			cliente += "Data Nasc........." + this.dataDeNascimento + "\n";
			cliente += "Sexo........." + this.sexo.getDescricao() + "\n";
			cliente += "Telefone........." + this.telefone + "\n";
			cliente += "Celular........." + this.celular + "\n";
			cliente += "Email........." + this.email + "\n";
		    cliente += "Ativo........." + (this.ativo ? "sim" : "Não") + "\n";
			return cliente;

	}
}
