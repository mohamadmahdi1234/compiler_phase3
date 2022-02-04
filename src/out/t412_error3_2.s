.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"

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
		jal A_f
		addi $sp, $sp, 0
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	A_f:
		sw $ra,0($sp)
		addi $sp,$sp,4
		li $t0, 2
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
