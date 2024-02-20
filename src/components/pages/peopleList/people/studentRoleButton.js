import('../../../../styles/peopleItem.css')

export const ButtonStudent = ({roles,callback}) => {
    return(
        <button onClick={callback} className={roles.includes('STUDENT') ? 'delit button-role' : 'button-role'}>
            <span className='buttonName'>Студент</span>
        </button>
    )
}