.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	global_main_doubles : .word 0
	global_main_NEW_ARRAY_0: .space 404

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
		la $a0, global_main_doubles
		l.s $f0, 0($a0)
		la $a3, 0($a0) 
		la $t0, global_main_NEW_ARRAY_0
		li $t2, 100
		sw $t2, 0($t0)
		s.s $f0, 0($a3)
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
