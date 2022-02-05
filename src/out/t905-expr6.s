.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	global_main_s : .word 0
	global_main_a : .word 0
	StringLiteral_01: .asciiz "Hello"
	StringLiteral_11: .asciiz " World!"
		StringLiteral_21:	.asciiz	"null World!"

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
		la $a0, global_main_a
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		la $a0, global_main_a
		lw $t0, 0($a0)
		la $t0, StringLiteral_11
	la	$t0, StringLiteral_21
		sw $t0, 0($a3)
		la $a0, global_main_a
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
