.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	global_main_s : .word 0
	StringLiteral_01: .asciiz "He had just walked in. Hey, he said, \"Aren't you a string?\""
	StringLiteral_11: .asciiz "Anounymous answered: \'No I'm an single dummy backslash. :(\'"
	StringLiteral_21: .asciiz "\"Oh! I am a single backslash too! I can be with you but I have a problem.\", he said."
	StringLiteral_31: .asciiz "\'What do you mean when you say \"problem\"?. We are just two backslashs like this:\\\\\', Anounymous said."
	StringLiteral_41: .asciiz "'I have a newline at my end. :| Because of this i can\'t be friend of any other backslashs (\\). In a single quote that is inside double quote i am like this: (\"\'\\\\n\'\")', Poor Anounymous said."
	StringLiteral_51: .asciiz "\"No problem! I have a newline before. I can understand you. Let's be together!\", he said."
	StringLiteral_61: .asciiz "\"Ok! :) but in a double quote home.\", They found each other forever: \"\\n\\\\\\n\""

.text
	.globl main

	main:
		jal global_main
		#END OF PROGRAM
		li $v0,10
		syscall
	runtime_error:
		li $v0, 4
		la $a0, error_run_time
		syscall
		#END OF PROGRAM
		li $v0,10
		syscall
	global_main:
		sw $ra,0($sp)
		addi $sp,$sp,4
		la $a0, global_main_s
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		la $t0, StringLiteral_01
		sw $t0, 0($a3)
		la $a0, global_main_s
		lw $t0, 0($a0)
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		la $a0, global_main_s
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		la $t0, StringLiteral_11
		sw $t0, 0($a3)
		la $a0, global_main_s
		lw $t0, 0($a0)
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		la $a0, global_main_s
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		la $t0, StringLiteral_21
		sw $t0, 0($a3)
		la $a0, global_main_s
		lw $t0, 0($a0)
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		la $a0, global_main_s
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		la $t0, StringLiteral_31
		sw $t0, 0($a3)
		la $a0, global_main_s
		lw $t0, 0($a0)
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		la $a0, global_main_s
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		la $t0, StringLiteral_41
		sw $t0, 0($a3)
		la $a0, global_main_s
		lw $t0, 0($a0)
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		la $a0, global_main_s
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		la $t0, StringLiteral_51
		sw $t0, 0($a3)
		la $a0, global_main_s
		lw $t0, 0($a0)
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		la $a0, global_main_s
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		la $t0, StringLiteral_61
		sw $t0, 0($a3)
		la $a0, global_main_s
		lw $t0, 0($a0)
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
